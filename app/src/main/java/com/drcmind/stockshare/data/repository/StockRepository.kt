package com.drcmind.stockshare.data.repository

import com.drcmind.stockshare.data.datasource.StockDao
import com.drcmind.stockshare.domain.model.Stock
import com.drcmind.stockshare.util.Result
import com.google.api.services.sheets.v4.Sheets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import javax.inject.Inject

class StockRepository @Inject constructor(
    private val googleSheetsApi : Sheets,
    private val dao: StockDao
) {
        fun getSheetsDataAndCacheThem(
            spreadSheetId : String = "1-hXrP-Xr_0K3R-9yhMFd2oy_MdncIY6uT3w1JABAgN4",
            spreadSheetRange : String = "Stocks!A2:H10"
        )= flow {
            emit(Result.Loading())

            val response = googleSheetsApi.spreadsheets().values()
                .get(spreadSheetId, spreadSheetRange).execute().getValues()

            val stocksListFromGoogleSheet = flowOf(response).flatMapMerge { it.asFlow() }
                .map {
                    Stock(
                        compte = it[0].toString(),
                        marque = it[1].toString(),
                        modele = it[2].toString(),
                        caractéristique = it[3].toString(),
                        prixNet = it[4].toString(),
                        commission = it[5].toString(),
                        imageLink = it[6].toString(),
                        stock = it[7].toString()
                    )
                }.toList()

            dao.insertAll(stocksListFromGoogleSheet.map { it.toStockEntity() })

            emit(Result.Success(true))
        }.catch {
            emit(Result.Error(data = null, throwable = it))
        }.flowOn(Dispatchers.IO)

    fun observeStock() = dao.observeStock()

}