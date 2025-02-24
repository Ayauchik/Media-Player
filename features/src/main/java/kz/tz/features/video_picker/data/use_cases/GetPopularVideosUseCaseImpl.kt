package kz.tz.features.video_picker.data.use_cases

import kz.tz.features.video_picker.domain.models.VideoModel
import kz.tz.features.video_picker.domain.repository.PexelsRepository
import kz.tz.features.video_picker.domain.use_cases.GetPopularVideosUseCase
import javax.inject.Inject

class GetPopularVideosUseCaseImpl @Inject constructor(
    private val repository: PexelsRepository
): GetPopularVideosUseCase {
    override suspend fun invoke(): List<VideoModel> {
        return repository.getPopularVideos()
    }

}
