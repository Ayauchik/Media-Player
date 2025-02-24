package kz.tz.features.video_picker.data.network.api

import kz.tz.features.BuildConfig
import kz.tz.features.video_picker.data.network.response.GetPopularVideosResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface PexelsApiService {
    @GET("videos/popular")
    suspend fun getRandomVideos(
        @Header("Authorization") apiKey: String,
        @Query("per_page") perPage: Int = 5
    ): GetPopularVideosResponse

    companion object {
        const val BASE_URL = BuildConfig.BASE_URL
    }
}
