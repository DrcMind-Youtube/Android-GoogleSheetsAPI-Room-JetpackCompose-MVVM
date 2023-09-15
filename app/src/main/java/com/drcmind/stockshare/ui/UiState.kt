package com.drcmind.stockshare.ui

import com.drcmind.stockshare.domain.model.Stock
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential

data class UiState(
    val isLoading : Boolean = false,
    val credential: GoogleAccountCredential? = null,
    val stocks : List<Stock> = emptyList(),
    val error : String? = null
)
