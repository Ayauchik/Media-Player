package kz.tz.features.video_picker.domain.models

data class VideoFileModel(
    val id: Int,
    val quality: String,
    val file_type: String,
    val link: String // Video URL
)