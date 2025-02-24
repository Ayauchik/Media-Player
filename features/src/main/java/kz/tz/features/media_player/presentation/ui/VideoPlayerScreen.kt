package kz.tz.features.media_player.presentation.ui

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.marginEnd
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavHostController
import kotlinx.serialization.json.Json.Default.configuration
import kz.tz.features.R
import kz.tz.features.media_player.presentation.view_model.VideoPlayerViewModel


@Composable
fun VideoPlayerScreen(
    videoUri: String,
    navController: NavHostController,
    viewModel: VideoPlayerViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isBuffering by viewModel.isBuffering.collectAsState()
    val exoPlayer = viewModel.getPlayer()


    var hasError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    fun isInternetAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(ConnectivityManager::class.java) ?: return false
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }



    // Initialize player only once
    LaunchedEffect(Unit) {
        if (isInternetAvailable()) {
            viewModel.initializePlayer(videoUri)
        } else {
            hasError = true
            errorMessage = "No internet connection"
        }
    }


    DisposableEffect(Unit) {

        val listener = object : androidx.media3.common.Player.Listener {
            override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                hasError = true
                errorMessage = "Playback error: ${error.localizedMessage ?: "Unknown error"}"
            }
        }

        exoPlayer.addListener(listener)

        onDispose {
            exoPlayer.removeListener(listener)
            viewModel.lastPosition = exoPlayer.currentPosition
            viewModel.isPlaying = exoPlayer.playWhenReady
        }
//        onDispose {
//            viewModel.lastPosition = exoPlayer.currentPosition
//            viewModel.isPlaying = exoPlayer.playWhenReady
//        }
    }

    fun toggleOrientation() {
        val activity = context as? android.app.Activity
        activity?.requestedOrientation =
            if (configuration.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT)
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            else
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                object : PlayerView(ctx) {
                    var rotateButton: ImageButton? = null

                    init {
                        player = exoPlayer
                        useController = true

                        rotateButton = ImageButton(ctx).apply {
                            setImageResource(android.R.drawable.ic_menu_rotate)
                            setBackgroundColor(Color.TRANSPARENT)
                            setOnClickListener { toggleOrientation() }
                            visibility = View.VISIBLE
                        }

                        addView(
                            rotateButton,
                            FrameLayout.LayoutParams(120, 120).apply {
                                gravity = Gravity.END or Gravity.TOP
                                marginEnd = 50
                                topMargin = 50
                            }
                        )

                        setControllerVisibilityListener(ControllerVisibilityListener { visibility ->
                            rotateButton?.visibility =
                                if (visibility == View.VISIBLE) View.VISIBLE else View.GONE
                        })
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        if (isBuffering && !hasError) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f))
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        if (hasError) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        if (isInternetAvailable()) {
                            hasError = false
                            viewModel.initializePlayer(videoUri)
                        }
                    }) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}
