package com.shahrukhamd.realestate.di

import androidx.databinding.ktx.BuildConfig
import com.shahrukhamd.realestate.data.api.AvivService
import com.shahrukhamd.realestate.data.repository.PropertyRepository
import com.shahrukhamd.realestate.data.repository.PropertyRepositoryImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideConverterFactory(): Converter.Factory {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        return MoshiConverterFactory.create(moshi)
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(converterFactory)
            .client(okHttpClient)
    }

    @Provides
    @Singleton
    fun provideAvivService(retrofitBuilder: Retrofit.Builder): AvivService {
        return retrofitBuilder
            .baseUrl("https://gsl-apps-technical-test.dignp.com/") // fixme url shouldn't be hardcoded
            .build()
            .create(AvivService::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun providePropertyRepository(avivService: AvivService): PropertyRepository = PropertyRepositoryImpl(avivService)
}