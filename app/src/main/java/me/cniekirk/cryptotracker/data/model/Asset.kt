package me.cniekirk.cryptotracker.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Asset(
    @SerialName("changePercent24Hr")
    val changePercent24Hr: String? = null,
    @SerialName("explorer")
    val explorer: String? = null,
    @SerialName("id")
    val id: String? = null,
    @SerialName("marketCapUsd")
    val marketCapUsd: String? = null,
    @SerialName("maxSupply")
    val maxSupply: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("priceUsd")
    val priceUsd: Double? = null,
    @SerialName("rank")
    val rank: String? = null,
    @SerialName("supply")
    val supply: String? = null,
    @SerialName("symbol")
    val symbol: String? = null,
    @SerialName("volumeUsd24Hr")
    val volumeUsd24Hr: String? = null,
    @SerialName("vwap24Hr")
    val vwap24Hr: String? = null
)