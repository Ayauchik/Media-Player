package kz.tz.features.video_picker.data.network.mapper

import kz.tz.features.video_picker.data.network.response.GetPopularVideosResponse
import kz.tz.features.video_picker.domain.models.VideoFileModel
import kz.tz.features.video_picker.domain.models.VideoModel

class VideoMapper {
    fun fromRemoteToDomain(response: GetPopularVideosResponse): List<VideoModel> {
        val videos = response.videos
        val videoModels = videos.map {
            VideoModel(
                id = it.id,
                name = "Video by ${it.user.name}",
                duration = it.duration,
                image = it.image,
                video_files = it.video_files.map { videoFile ->
                    VideoFileModel(
                        id = videoFile.id,
                        quality = videoFile.quality,
                        file_type = videoFile.file_type,
                        link = videoFile.link
                    )
                }
            )
        }

        return videoModels
    }
}