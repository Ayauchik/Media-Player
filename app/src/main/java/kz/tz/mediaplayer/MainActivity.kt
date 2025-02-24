package kz.tz.mediaplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import kz.tz.features.media_player.presentation.ui.VideoPlayerScreen
import kz.tz.features.video_picker.presentation.ui.VideoPickerScreen
import kz.tz.mediaplayer.ui.theme.MediaPlayerTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MediaPlayerTheme {
                VideoNavigation()
            }
        }
    }
}


@Composable
fun VideoNavigation() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "videoPicker",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("videoPicker") {
                VideoPickerScreen(navController = navController)
            }
            composable(
                "videoPlayer/{videoUri}",
                arguments = listOf(navArgument("videoUri") { type = NavType.StringType })
            ) { backStackEntry ->
                val videoUri = backStackEntry.arguments?.getString("videoUri")
                if (videoUri != null) {
                    VideoPlayerScreen(videoUri, navController)
                }
            }
        }
    }
}
