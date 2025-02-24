package kz.tz.features.video_picker.domain.use_cases

import kz.tz.features.video_picker.domain.models.VideoModel

interface GetPopularVideosUseCase {
    suspend fun invoke(): List<VideoModel>
}