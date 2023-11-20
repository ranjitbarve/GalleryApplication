package com.ranjit.galleryapplication

import android.content.Context
import com.ranjit.data.BuildConfig.API_URL
import com.ranjit.data.base.AuthenticationInterceptor
import com.ranjit.data.base.NetworkConnectionInterceptor
import com.ranjit.data.logger.utils.Logger
import com.ranjit.galleryapplication.data.remote.RetrofitService
import com.ranjit.galleryapplication.data.repository.GalleryRepositoryImpl
import com.ranjit.galleryapplication.domain.usecase.GetGalleryUseCase
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Provides
    @Singleton
    fun providesAuthenticationInterceptor(): AuthenticationInterceptor = AuthenticationInterceptor()

    @Singleton
    @Provides
    fun provideNetworkInterceptor(@ApplicationContext context: Context): NetworkConnectionInterceptor =
        NetworkConnectionInterceptor(context)

    @Provides
    @Singleton
    fun provideLoggingInterceptor(logger: Logger): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            try {
                logger.debug("Retrofit :: ", message)
            } catch (ignore: Exception) {
            }
        }
        if (com.ranjit.data.BuildConfig.DEBUG) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE)
        }
        return loggingInterceptor
    }

    @Singleton
    @Provides
    fun provideClient(
        networkConnectionInterceptor: NetworkConnectionInterceptor,
        authenticationInterceptor: AuthenticationInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder().apply {
        if (BuildConfig.DEBUG) {
            val logger = object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) = Timber.d(message)
            }
            val interceptor = HttpLoggingInterceptor(logger).apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            this.addInterceptor(interceptor)
            this.addInterceptor(authenticationInterceptor)
            this.addInterceptor(networkConnectionInterceptor)
        }
            this.addInterceptor(loggingInterceptor)
    }.build()

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient,
                        moshi: Moshi): Retrofit = Retrofit.Builder()
        .baseUrl(API_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(client)
        .build()

    @Singleton
    @Provides
    internal fun provideApi(retrofit: Retrofit): RetrofitService =
        retrofit.create(RetrofitService::class.java)

    @Singleton
    @Provides
    internal fun provideGetGalleryUseCase(
        galleryRepository: GalleryRepositoryImpl
    ): GetGalleryUseCase =
        GetGalleryUseCase(galleryRepository)
}