package me.cniekirk.cryptotracker

import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import me.cniekirk.cryptotracker.ui.detail.CoinDetailViewModel
import me.cniekirk.cryptotracker.ui.list.CoinListViewModel
import me.cniekirk.cryptotracker.ui.theme.CryptoTrackerTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalAnimationApi
@HiltAndroidTest
class CoinListTest {

    @get:Rule(order = 1)
    var hiltTestRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    var composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltTestRule.inject()
        composeTestRule.setContent {
            CryptoTrackerTheme {
                CryptoApp(
                    composeTestRule.activity.viewModels<CoinListViewModel>().value,
                    composeTestRule.activity.viewModels<CoinDetailViewModel>().value
                )
            }
        }
    }

    @Test
    fun app_Launches() {
        composeTestRule.onNodeWithText("Crypto prices").assertExists()
    }

    @Test
    fun app_opensAsset() {
        println(composeTestRule.onRoot().printToString())
        composeTestRule.onNodeWithTag("coin list").onChildren()[0].performClick()

        println(composeTestRule.onRoot().printToString())
        composeTestRule.onNodeWithTag("coin title").assertExists()
    }
}