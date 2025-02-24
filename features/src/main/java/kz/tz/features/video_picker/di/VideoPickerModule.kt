package kz.tz.features.video_picker.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kz.tz.features.BuildConfig
import kz.tz.features.video_picker.data.network.api.PexelsApiService
import kz.tz.features.video_picker.data.network.mapper.VideoMapper
import kz.tz.features.video_picker.data.repository.PexelsRepositoryImpl
import kz.tz.features.video_picker.data.use_cases.GetPopularVideosUseCaseImpl
import kz.tz.features.video_picker.domain.repository.PexelsRepository
import kz.tz.features.video_picker.domain.use_cases.GetPopularVideosUseCase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object VideoPickerModule {

    @Provides
    @Singleton
    fun providePexelsApiService(): PexelsApiService {
        return Retrofit.Builder()
            .baseUrl(PexelsApiService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PexelsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideVideoMapper(): VideoMapper = VideoMapper()

    @Provides
    @Singleton
    fun providePexelsRepository(
        apiService: PexelsApiService,
        videoMapper: VideoMapper
    ): PexelsRepository {
        return PexelsRepositoryImpl(apiService, videoMapper, BuildConfig.PEXELS_API_KEY)
    }

    @Provides
    @Singleton
    fun provideGetPopularVideosUseCase(repository: PexelsRepository): GetPopularVideosUseCase {
        return GetPopularVideosUseCaseImpl(repository)
    }
}
