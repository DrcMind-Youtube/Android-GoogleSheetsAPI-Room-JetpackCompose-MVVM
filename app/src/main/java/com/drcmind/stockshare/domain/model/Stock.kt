package com.drcmind.stockshare.domain.model

import com.drcmind.stockshare.data.datasource.StockEntity

data class Stock(
    val compte : String,
    val marque : String,
    val modele : String,
    val caractéristique	 : String,
    val prixNet : String,
    val commission : String,
    val imageLink : String,
    val stock : String
){
    fun toStockEntity() = StockEntity(
        compte, marque, modele, caractéristique, prixNet, commission, imageLink, stock)
}
