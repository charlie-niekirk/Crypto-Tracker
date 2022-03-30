package me.cniekirk.cryptotracker.data

import me.cniekirk.cryptotracker.data.model.Asset

class AssetFactory {

    fun createAsset(name: String): Asset {
        return Asset(name = name)
    }
}