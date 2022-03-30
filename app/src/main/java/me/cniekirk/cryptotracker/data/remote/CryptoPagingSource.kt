package me.cniekirk.cryptotracker.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import me.cniekirk.cryptotracker.data.model.Asset
import javax.inject.Inject

const val NETWORK_PAGE_SIZE = 25
private const val INITIAL_LOAD_SIZE = 0

class CryptoPagingSource @Inject constructor(
    private val coinCapApi: CoinCapApi
): PagingSource<Int, Asset>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Asset> {
        val position = params.key ?: INITIAL_LOAD_SIZE
        val offset = if (params.key != null) position * NETWORK_PAGE_SIZE else INITIAL_LOAD_SIZE

        return try {
            val rawResponse = coinCapApi.getAssets(limit = params.loadSize, offset = offset)
            val coinList = rawResponse.body()?.data
            val nextKey = coinList?.let {
                if (it.isEmpty()) {
                    null
                } else {
                    position + (params.loadSize / NETWORK_PAGE_SIZE)
                }
            } ?: run {
                null
            }
            coinList?.let {
                LoadResult.Page(
                    data = it,
                    prevKey = null,
                    nextKey = nextKey
                )
            } ?: run {
                LoadResult.Error(Exception("List was empty"))
            }
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Asset>): Int? {
        return null
    }
}