package kz.tz.features.video_picker.domain.models


data class VideoModel(
    val id: Int,
    val name: String,
    val duration: Int, // Duration in seconds
    val image: String, // Thumbnail URL
    val video_files: List<VideoFileModel>
)