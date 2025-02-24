package kz.tz.features.video_picker.data.repository

import kz.tz.features.video_picker.data.network.api.PexelsApiService
import kz.tz.features.video_picker.data.network.mapper.VideoMapper
import kz.tz.features.video_picker.domain.models.VideoModel
import kz.tz.features.video_picker.domain.repository.PexelsRepository
import javax.inject.Inject

class PexelsRepositoryImpl @Inject constructor(
    private val apiService: PexelsApiService,
    private val videoMapper: VideoMapper,
    private val apiKey: String
) : PexelsRepository {
    override suspend fun getPopularVideos(): List<VideoModel> {
//        val apiKey = BuildConfig.PEXELS_API_KEY

        val remote =
            apiService.getRandomVideos(apiKey = apiKey)
        return videoMapper.fromRemoteToDomain(remote)
    }
}
