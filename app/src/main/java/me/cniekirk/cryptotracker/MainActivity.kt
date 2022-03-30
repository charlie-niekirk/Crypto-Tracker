package me.cniekirk.cryptotracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import me.cniekirk.cryptotracker.ui.detail.CoinDetail
import me.cniekirk.cryptotracker.ui.detail.CoinDetailViewModel
import me.cniekirk.cryptotracker.ui.list.CoinListViewModel
import me.cniekirk.cryptotracker.ui.list.CryptoList
import me.cniekirk.cryptotracker.ui.theme.CryptoTrackerTheme

@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CryptoTrackerTheme {
                val viewModel = hiltViewModel<CoinListViewModel>()
                val detailViewModel = hiltViewModel<CoinDetailViewModel>()
                CryptoApp(viewModel, detailViewModel)
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun CryptoApp(viewModel: CoinListViewModel, detailViewModel: CoinDetailViewModel) {
    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "list") {
            composable("list") {
                CryptoList(viewModel) {
                    navController.navigate("detail/$it")
                }
            }
            composable("detail/{coin}") { backStackEntry ->
                CoinDetail(detailViewModel, backStackEntry.arguments?.getString("coin") ?: "")
            }
        }
    }
}