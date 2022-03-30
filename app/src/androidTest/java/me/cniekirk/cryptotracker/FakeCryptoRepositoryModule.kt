package me.cniekirk.cryptotracker

import androidx.paging.PagingData
import androidx.paging.PagingSource
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import me.cniekirk.cryptotracker.data.model.Asset
import me.cniekirk.cryptotracker.data.model.SocketEvent
import me.cniekirk.cryptotracker.data.repo.CryptoDataRepository
import me.cniekirk.cryptotracker.di.NetworkModule
import javax.inject.Singleton

@TestInstallIn(components = [SingletonComponent::class],
    replaces = [NetworkModule::class])
@Module
object FakeCryptoRepositoryModule {
    @Singleton
    @Provides
    fun provideFakeCryptoRepository() = object : CryptoDataRepository {
        override fun getCryptoList(): Flow<PagingData<Asset>> {
            return flowOf(PagingData.from(listOf(Asset(name = "BTC", priceUsd = 11.00))))
        }
        override fun socketEventsFlow(coin: String): Flow<SocketEvent?> {
            return flowOf(
                SocketEvent.StringMessage("1"),
                SocketEvent.StringMessage("2"),
                SocketEvent.StringMessage("3")
            )
        }
    }
}