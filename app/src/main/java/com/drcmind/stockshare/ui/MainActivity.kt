package com.drcmind.stockshare.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import com.drcmind.stockshare.ui.theme.StockShareTheme
import com.drcmind.stockshare.util.GoogleAuthHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var googleAuthHelper: GoogleAuthHelper

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StockShareTheme {

                val signInIntent = googleAuthHelper.requestSignIn()
                val viewModel : MainViewModel = hiltViewModel()
                val uiState = viewModel.uiState.value

                if(googleAuthHelper.getSignedInUser()==null){
                    val launcher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.StartActivityForResult(),
                        onResult = {result->
                            if(result.resultCode == RESULT_OK){
                                lifecycleScope.launch {
                                    val credential = googleAuthHelper.signIn(
                                        result.data ?: return@launch
                                    )
                                    viewModel.signInSuccess(credential)
                                }
                            }
                        }
                    )
                    LaunchedEffect(key1 = Unit ){
                        launcher.launch(signInIntent)
                    }
                }else{
                    viewModel.setupCurrentUser(googleAuthHelper.getSignedInUser()!!)
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                        Scaffold(
                            topBar = {
                                CenterAlignedTopAppBar(
                                    title = {
                                        Text(text = "StockShareApp", fontWeight = FontWeight.ExtraBold)
                                    },
                                    actions =  {
                                        IconButton(onClick = {
                                            viewModel.getSheetDataAndCacheThem()
                                        }) {
                                            Icon(
                                                imageVector = Icons.Filled.Refresh,
                                                contentDescription = null)
                                        }
                                    }
                                )
                            }
                        ) {paddingValues ->
                            Box(modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)) {

                                if(uiState.isLoading || uiState.credential==null){
                                    CircularProgressIndicator(
                                        Modifier.align(Alignment.Center)
                                    )
                                }else if (uiState.error==null){
                                    LazyColumn(Modifier.fillMaxSize()){
                                        items(uiState.stocks){stock->
                                            StockItemUi(
                                                stock = stock,
                                                onStockShare = {stock->
                                                    shareStock(
                                                        "${stock.imageLink}\n" +
                                                                "${stock.marque} ${stock.modele}\n" +
                                                                "${stock.caractéristique}\n" +
                                                                "Prix net : ${stock.prixNet}\n" +
                                                                "Commission : ${stock.commission}"
                                                    )
                                                }
                                            )
                                        }
                                    }
                                }else{
                                    Text(text = "Error\n\n${uiState.error}",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                        )
                                }

                            }
                        }
                }
            }
        }
    }
}

fun MainActivity.shareStock(data:String){
    val sendIntent : Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, data)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent,null)
    startActivity(shareIntent)

}
