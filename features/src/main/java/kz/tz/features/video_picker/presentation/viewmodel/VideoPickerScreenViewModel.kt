package kz.tz.features.video_picker.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kz.tz.features.video_picker.domain.models.VideoModel
import kz.tz.features.video_picker.domain.use_cases.GetPopularVideosUseCase
import javax.inject.Inject

@HiltViewModel
class VideoPickerScreenViewModel @Inject constructor(
    private val getPopularVideosUseCase: GetPopularVideosUseCase,
    application: Application
) : AndroidViewModel(application) {

    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("video_cache", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val _videos = MutableStateFlow<List<VideoModel>>(emptyList())
    val videos: StateFlow<List<VideoModel>> get() = _videos

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    init {
        fetchVideos()
    }

    private fun fetchVideos() {
        viewModelScope.launch {
            _isRefreshing.value = true
            delay(1000)  // Simulate network delay
            try {
                val cachedVideos = loadCachedVideos()
                if (cachedVideos.isNotEmpty()) {
                    _videos.value = cachedVideos
                } else {
                    val newVideos = getPopularVideosUseCase.invoke()
                    _videos.value = newVideos
                    saveVideosToCache(newVideos)
                }

                _isRefreshing.value = false
            } catch (e: Exception) {
                e.printStackTrace()
                _isRefreshing.value = false
            }
        }


    }

    fun refreshVideos() {
        fetchVideos() // Refresh logic
    }

    fun saveVideosToCache(videos: List<VideoModel>) {
        val json = gson.toJson(videos)
        sharedPreferences.edit().putString("cached_videos", json).apply()
    }

    fun loadCachedVideos(): List<VideoModel> {
        val json = sharedPreferences.getString("cached_videos", null)
        return if (json != null) {
            val type = object : TypeToken<List<VideoModel>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }
}