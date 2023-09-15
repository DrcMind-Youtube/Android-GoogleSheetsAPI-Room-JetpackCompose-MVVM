package com.drcmind.stockshare.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.drcmind.stockshare.domain.model.Stock

@Composable
fun StockItemUi(
    stock: Stock,
    onStockShare : (Stock)->Unit
){
    Card(modifier = Modifier.padding(16.dp)) {
        Column(Modifier.fillMaxWidth()) {
            AsyncImage(
                model = stock.imageLink,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                Text(
                    text = "${stock.marque} ${stock.modele}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stock.caractéristique,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${stock.prixNet} USD | ${stock.commission} USD",
                    style = MaterialTheme.typography.bodyLarge
                )

                Row(
                    modifier =  Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Stock dispo : ${stock.stock}")
                    IconButton(onClick = { onStockShare(stock) }) {
                        Icon(imageVector = Icons.Filled.Share, contentDescription = null)
                    }
                }
            }
        }
    }
}