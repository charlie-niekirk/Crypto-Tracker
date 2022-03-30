package me.cniekirk.cryptotracker.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import me.cniekirk.cryptotracker.data.repo.CryptoDataRepository
import javax.inject.Inject

@HiltViewModel
class CoinListViewModel @Inject constructor(
    cryptoDataRepository: CryptoDataRepository
) : ViewModel() {

    val coinList = cryptoDataRepository.getCryptoList().cachedIn(viewModelScope)
}