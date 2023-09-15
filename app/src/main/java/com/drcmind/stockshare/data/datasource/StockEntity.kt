package com.drcmind.stockshare.data.datasource

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.drcmind.stockshare.domain.model.Stock

@Entity
data class StockEntity(
    @PrimaryKey
    val compte : String,
    val marque : String,
    val modele : String,
    val caractéristique	 : String,
    val prixNet : String,
    val commission : String,
    val imageLink : String,
    val stock : String
){
    fun toStock() = Stock(
        compte, marque, modele, caractéristique, prixNet, commission, imageLink, stock)
}
