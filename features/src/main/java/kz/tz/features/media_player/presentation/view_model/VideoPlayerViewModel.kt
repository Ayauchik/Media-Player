package kz.tz.features.media_player.presentation.view_model

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    private val exoPlayer: ExoPlayer
) : ViewModel() {

    private val _isBuffering = MutableStateFlow(false)
    val isBuffering: StateFlow<Boolean> = _isBuffering

    var lastPosition by mutableLongStateOf(0L)
    var isPlaying by mutableStateOf(true)
    private var isMediaSet = false

    init {
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                _isBuffering.value = state == Player.STATE_BUFFERING
            }
        })
    }

    fun initializePlayer(videoUri: String) {
        if (!isMediaSet) {
            exoPlayer.setMediaItem(MediaItem.fromUri(Uri.parse(videoUri)))
            exoPlayer.prepare()
            exoPlayer.seekTo(lastPosition)
            exoPlayer.playWhenReady = isPlaying
            isMediaSet = true
        }
    }

    fun getPlayer(): ExoPlayer = exoPlayer

    override fun onCleared() {
        lastPosition = exoPlayer.currentPosition
        isPlaying = exoPlayer.playWhenReady
        exoPlayer.release()
        super.onCleared()
    }
}
