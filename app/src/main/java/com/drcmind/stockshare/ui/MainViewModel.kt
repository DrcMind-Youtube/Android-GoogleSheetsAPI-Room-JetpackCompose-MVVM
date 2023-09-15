package com.drcmind.stockshare.ui

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drcmind.stockshare.data.repository.StockRepository
import com.drcmind.stockshare.util.Result
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: StockRepository
) : ViewModel() {
    val uiState = mutableStateOf(UiState())

    init {
        observeStockFromLocal()
    }

    fun signInSuccess(credential: GoogleAccountCredential){
        uiState.value = uiState.value.copy(
            credential = credential
        )
        getSheetDataAndCacheThem()
    }

    fun setupCurrentUser(credential: GoogleAccountCredential){
        uiState.value = uiState.value.copy(
            credential = credential
        )
    }

    fun getSheetDataAndCacheThem(){
        viewModelScope.launch(Dispatchers.Main) {
            repository.getSheetsDataAndCacheThem().collect{result->
                when(result){
                    is Result.Loading->{
                        uiState.value = uiState.value.copy(
                            isLoading = true
                        )
                    }
                    is Result.Success->{
                        uiState.value = uiState.value.copy(
                            isLoading = false
                        )
                    }
                    is Result.Error->{
                        uiState.value = uiState.value.copy(
                            isLoading = false,
                            error = result.throwable!!.message
                        )
                        result.throwable.printStackTrace()
                    }
                }
                Log.d("STOCKSHARE", result.toString())
            }
        }
    }

    private fun observeStockFromLocal(){
        viewModelScope.launch {
            repository.observeStock().collect{list->
                uiState.value = uiState.value.copy(
                    isLoading = false,
                    stocks = list.map { it.toStock() }
                )
            }
        }
    }
}