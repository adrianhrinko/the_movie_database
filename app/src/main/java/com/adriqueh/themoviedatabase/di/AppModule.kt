package com.adriqueh.themoviedatabase.di

import com.adriqueh.themoviedatabase.BuildConfig
import com.adriqueh.themoviedatabase.data.api.ApiService
import com.adriqueh.themoviedatabase.data.api.ApiServiceImpl
import com.adriqueh.themoviedatabase.data.api.MovieApi
import com.adriqueh.themoviedatabase.data.repository.MainRepository
import com.adriqueh.themoviedatabase.data.repository.MainRepositoryImpl
import com.adriqueh.themoviedatabase.misc.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    fun provideBaseUrl() = Constants.API_BASE_URL

    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG){
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }else{
        OkHttpClient
            .Builder()
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL:String): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideMovieApi(retrofit: Retrofit) = retrofit.create(MovieApi::class.java)

    @Provides
    @Singleton
    fun provideApiService(service: ApiServiceImpl): ApiService = service

    @Provides
    @Singleton
    fun provideRepository(repository: MainRepositoryImpl): MainRepository = repository
}