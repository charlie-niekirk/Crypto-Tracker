package me.cniekirk.cryptotracker.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import me.cniekirk.cryptotracker.data.model.SocketEvent
import me.cniekirk.cryptotracker.data.repo.CryptoDataRepository
import me.cniekirk.cryptotracker.di.IoDispatcher
import timber.log.Timber
import javax.inject.Inject

/**
 * [ViewModel] that provides price updates to the coin detail page
 *
 * @author Charlie Niekirk
 */
@HiltViewModel
class CoinDetailViewModel @Inject constructor(
    private val cryptoDataRepository: CryptoDataRepository,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _socketMessages = MutableStateFlow(LivePrice(PriceState.SAME, 0.0))
    val socketMessages: Flow<LivePrice> = _socketMessages

    private val coin: String? = savedStateHandle.get<String>("coin")

    init {
        trackCoinPrice(coin ?: "")
    }

    private fun trackCoinPrice(coin: String) {
        viewModelScope.launch(coroutineDispatcher) {
            cryptoDataRepository.socketEventsFlow(coin)
                .takeWhile { it !is SocketEvent.CloseEvent }
                .cancellable()
                .collect {
                    currentCoroutineContext().ensureActive()
                    when (it) {
                        is SocketEvent.StringMessage -> {
                            val newPrice = it.content.replace("\"", "").toDouble()
                            val priceState = when {
                                _socketMessages.value.price == newPrice -> {
                                    PriceState.SAME
                                }
                                _socketMessages.value.price > newPrice -> {
                                    PriceState.DOWN
                                }
                                else -> {
                                    PriceState.UP
                                }
                            }
                            _socketMessages.tryEmit(LivePrice(priceState, newPrice))
                        }
                        else -> {
                            Timber.e("An error occurred!")
                        }
                    }
                }
        }
    }
}