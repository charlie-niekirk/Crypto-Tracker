package me.cniekirk.cryptotracker.ui.detail

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import me.cniekirk.cryptotracker.ui.theme.spacing
import java.math.RoundingMode

@ExperimentalAnimationApi
@Composable
fun CoinDetail(viewModel: CoinDetailViewModel, coin: String) {
    val state = viewModel.socketMessages.collectAsState(initial = LivePrice(PriceState.SAME, 0.0))
    CoinDetailContent(state, coin)
}

@ExperimentalAnimationApi
@Composable
fun CoinDetailContent(price: State<LivePrice>, coin: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            text = coin.capitalize(Locale.current),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = androidx.compose.material.MaterialTheme.spacing.large)
                .testTag("coin title")
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(
                top = androidx.compose.material.MaterialTheme.spacing.large
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Live Price:",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(
                    start = androidx.compose.material.MaterialTheme.spacing.large
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            val animatedColor = animateColorAsState(
                when (price.value.priceState) {
                    PriceState.UP -> Color.Green
                    PriceState.SAME -> Color.Transparent
                    PriceState.DOWN -> Color.Red
                }
            )
            Text(
                text = "$${price.value.price.toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toPlainString()}",
                style = MaterialTheme.typography.titleMedium.copy(),
                modifier = Modifier.padding(
                    end = androidx.compose.material.MaterialTheme.spacing.large
                ).background(animatedColor.value).padding(androidx.compose.material.MaterialTheme.spacing.medium)
            )
        }
    }
}