package me.cniekirk.cryptotracker.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import me.cniekirk.cryptotracker.data.model.Asset
import me.cniekirk.cryptotracker.data.model.SocketEvent
import me.cniekirk.cryptotracker.data.remote.CoinCapApi
import me.cniekirk.cryptotracker.data.remote.CryptoPagingSource
import me.cniekirk.cryptotracker.data.remote.NETWORK_PAGE_SIZE
import okhttp3.*
import timber.log.Timber
import javax.inject.Inject

class CryptoDataRepositoryImpl @Inject constructor(
    private val coinCapApi: CoinCapApi,
    private val okHttpClient: OkHttpClient,
    private val json: Json
): CryptoDataRepository {

    private lateinit var socket: WebSocket

    override fun getCryptoList(): Flow<PagingData<Asset>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                CryptoPagingSource(coinCapApi)
            }
        ).flow
    }

    override fun socketEventsFlow(coin: String): Flow<SocketEvent?> = callbackFlow {
        val socketListener = object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                val update = json.parseToJsonElement(text)
                val result = update.jsonObject[update.jsonObject.keys.first()]
                trySend(SocketEvent.StringMessage(result.toString()))
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                trySend(SocketEvent.CloseEvent(code, reason))
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                trySend(SocketEvent.Error(t))
            }
        }
        attachSocketListener(socketListener, coin)
        awaitClose { socket.cancel() }
    }

    private fun attachSocketListener(socketListener: WebSocketListener, coin: String) {
        val request = Request.Builder().url("wss://ws.coincap.io/prices?assets=$coin").build()
        socket = okHttpClient.newWebSocket(request, socketListener)
    }
}