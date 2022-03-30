package me.cniekirk.cryptotracker.data.repo

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import me.cniekirk.cryptotracker.data.model.Asset
import me.cniekirk.cryptotracker.data.model.SocketEvent

interface CryptoDataRepository {

    fun getCryptoList(): Flow<PagingData<Asset>>

    fun socketEventsFlow(coin: String): Flow<SocketEvent?>
}