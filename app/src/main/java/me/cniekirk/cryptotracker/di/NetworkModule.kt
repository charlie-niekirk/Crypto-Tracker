package me.cniekirk.cryptotracker.di

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import me.cniekirk.cryptotracker.data.remote.CoinCapApi
import me.cniekirk.cryptotracker.data.repo.CryptoDataRepository
import me.cniekirk.cryptotracker.data.repo.CryptoDataRepositoryImpl
import okhttp3.Cache
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideCache(@ApplicationContext context: Context): Cache {
        return Cache(context.cacheDir, 10 * 1024 * 1024)
    }

    @Provides
    @Singleton
    fun provideOkHttp(cache: Cache): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .cache(cache)
            .build()
    }

    @Provides
    @Singleton
    fun  provideJsonConverter() = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }

    /**
     * Provides a [Retrofit] instance
     *
     * @param okHttpClient a [Lazy] [OkHttpClient] to offload initialisation to a background thread
     * @param json the [Json] instance to (de)serialise API responses
     * @return [Retrofit] instance
     */
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: Lazy<OkHttpClient>, json: Json): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.coincap.io/")
            .callFactory { okHttpClient.get().newCall(it) }
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    fun provideCoinCapApi(retrofit: Retrofit): CoinCapApi = retrofit.create(CoinCapApi::class.java)

    @Provides
    @Singleton
    fun provideCryptoDataRepository(cryptoDataRepositoryImpl: CryptoDataRepositoryImpl): CryptoDataRepository
        = cryptoDataRepositoryImpl
}