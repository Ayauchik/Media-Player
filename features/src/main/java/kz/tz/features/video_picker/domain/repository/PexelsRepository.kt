package kz.tz.features.video_picker.domain.repository

import kz.tz.features.video_picker.domain.models.VideoModel


interface PexelsRepository {
    suspend fun getPopularVideos(): List<VideoModel>
}
