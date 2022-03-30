package me.cniekirk.cryptotracker

import androidx.paging.PagingSource
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import me.cniekirk.cryptotracker.data.AssetFactory
import me.cniekirk.cryptotracker.data.model.Asset
import me.cniekirk.cryptotracker.data.model.AssetData
import me.cniekirk.cryptotracker.data.remote.CoinCapApi
import me.cniekirk.cryptotracker.data.remote.CryptoPagingSource
import me.cniekirk.cryptotracker.data.remote.NETWORK_PAGE_SIZE
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class CryptoPagingSourceTest {

    private val assetFactory = AssetFactory()
    private val assets = mutableListOf<Asset>()

    private val mockApi = mockk<CoinCapApi>()
    private val mockErrorApi = mockk<CoinCapApi>()

    private val error = RuntimeException("500", Throwable())

    @Before
    fun setup() {
        repeat(25) { assets.add(assetFactory.createAsset("BTC")) }
        coEvery {
            mockApi.getAssets(
                limit = NETWORK_PAGE_SIZE,
                offset = 0
            )
        } returns Response.success(
            AssetData(assets, 11111111)
        )
        coEvery {
            mockErrorApi.getAssets(
                limit = NETWORK_PAGE_SIZE,
                offset = 0
            )
        } throws error
    }

    @Test
    fun loadReturnsPageWhenOnSuccessfulLoadOfPageKeyedData() = runTest {
        val pagingSource = CryptoPagingSource(mockApi)
        assertEquals(
            PagingSource.LoadResult.Page(
                data = assets,
                prevKey = null,
                nextKey = 1
            ),
            pagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = NETWORK_PAGE_SIZE,
                    placeholdersEnabled = false
                )
            )
        )
    }

    @Test
    fun loadReturnsErrorWhenOnErrorLoadOfPageKeyedData() = runTest {
        val pagingSource = CryptoPagingSource(mockErrorApi)
        assertEquals(
            PagingSource.LoadResult.Error<Int, Asset>(error),
            pagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = NETWORK_PAGE_SIZE,
                    placeholdersEnabled = false
                )
            )
        )
    }
}