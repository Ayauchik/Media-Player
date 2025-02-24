package kz.tz.features.video_picker.presentation.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kz.tz.features.video_picker.domain.models.VideoModel
import kz.tz.features.video_picker.presentation.viewmodel.VideoPickerScreenViewModel
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun VideoPickerScreen(
    viewModel: VideoPickerScreenViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val videos by viewModel.videos.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.refreshVideos() }
    )


    val isFirstLoad = videos.isEmpty()
    val allImagesLoaded by remember(videos) {
        derivedStateOf { videos.isNotEmpty() && videos.all { it.image.isNotEmpty() } }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Popular Videos") })
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
            when {
                isFirstLoad || !allImagesLoaded -> {
                    LoadingIndicator()
                }

                else -> {
                    VideoList(videos, navController)

                    PullRefreshIndicator(
                        refreshing = isRefreshing,
                        state = pullRefreshState,
                        modifier = Modifier.align(androidx.compose.ui.Alignment.TopCenter)
                    )
                }
            }
        }
    }
}


@Composable
fun VideoList(
    videos: List<VideoModel>,
    navController: NavHostController
) {

    val context = LocalContext.current
    var allImagesLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(videos) {
        val imageLoader = coil.ImageLoader(context)
        videos.forEach { video ->
            val request = coil.request.ImageRequest.Builder(context)
                .data(video.image)
                .build()
            imageLoader.execute(request)
        }
        allImagesLoaded = true
    }

    if (!allImagesLoaded) {
        LoadingIndicator()
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(videos) { video ->
                VideoCard(
                    video,
                    onCardClick = { navController.navigate("videoPlayer/${Uri.encode(video.video_files[0].link)}") })
            }
        }
    }
}


@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

