package kz.tz.features.video_picker.data.network.response

data class GetPopularVideosResponse(
    val next_page: String,
    val page: Int,
    val per_page: Int,
    val total_results: Int,
    val url: String,
    val videos: List<Video>
)