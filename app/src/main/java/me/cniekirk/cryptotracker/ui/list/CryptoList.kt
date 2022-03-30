package me.cniekirk.cryptotracker.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import me.cniekirk.cryptotracker.data.model.Asset
import me.cniekirk.cryptotracker.ui.theme.spacing
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun CryptoList(viewModel: CoinListViewModel, onClick: (String) -> Unit) {
    val coins = viewModel.coinList.collectAsLazyPagingItems()
    CryptoListContent(coins, onClick)
}

@Composable
fun CryptoListContent(lazyPagingItems: LazyPagingItems<Asset>, onClick: (String) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Crypto prices",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(
                top = androidx.compose.material.MaterialTheme.spacing.large,
                bottom = androidx.compose.material.MaterialTheme.spacing.large
            )
        )
        LazyColumn(modifier = Modifier.testTag("coin list")) {
            if (lazyPagingItems.loadState.refresh == LoadState.Loading) {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                }
            }

            itemsIndexed(lazyPagingItems) { _, item ->
                item?.let { CoinItem(it, onClick) }
                Divider()
            }

            if (lazyPagingItems.loadState.append == LoadState.Loading) {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
fun CoinItem(asset: Asset, onClick: (String) -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick(asset.name!!.lowercase()) }) {
        Column {
            Text(
                text = asset.symbol ?: "",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(
                    start = androidx.compose.material.MaterialTheme.spacing.large,
                    top = androidx.compose.material.MaterialTheme.spacing.large
                )
            )
            Text(
                text = "$${BigDecimal(asset.priceUsd!!).setScale(2, RoundingMode.HALF_EVEN).toPlainString()}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(
                    start = androidx.compose.material.MaterialTheme.spacing.large,
                    top = androidx.compose.material.MaterialTheme.spacing.small,
                    bottom = androidx.compose.material.MaterialTheme.spacing.large,
                )
            )
        }
    }
}