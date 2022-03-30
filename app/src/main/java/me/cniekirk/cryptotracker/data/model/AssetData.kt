package me.cniekirk.cryptotracker.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AssetData(
    @SerialName("data")
    val data: List<Asset>? = null,
    @SerialName("timestamp")
    val timestamp: Long? = null
)