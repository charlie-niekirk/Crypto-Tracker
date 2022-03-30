package me.cniekirk.cryptotracker.ui.detail

enum class PriceState {
    UP,
    SAME,
    DOWN
}

data class LivePrice(
    val priceState: PriceState,
    val price: Double
)
