package com.drcmind.stockshare.di

import android.content.Context
import androidx.room.Room
import com.drcmind.stockshare.R
import com.drcmind.stockshare.data.datasource.StockDatabase
import com.drcmind.stockshare.util.GoogleAuthHelper
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.sheets.v4.Sheets
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun providesGoogleAuthHelper(@ApplicationContext context: Context): GoogleAuthHelper {
        return GoogleAuthHelper(context)
    }

    @Provides
    fun providesGoogleSheetApi(
        @ApplicationContext context: Context,
        googleAuthHelper: GoogleAuthHelper
    ) : Sheets{
        val jsonFactory = JacksonFactory.getDefaultInstance()
        val httpTransport = NetHttpTransport()

        return Sheets.Builder(httpTransport, jsonFactory, googleAuthHelper.getSignedInUser())
            .setApplicationName(context.getString(R.string.app_name))
            .build()
    }

    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext context: Context): StockDatabase{
        return  Room.databaseBuilder(
            context.applicationContext,
            StockDatabase::class.java,
            "stockDB.db"
        ).build()
    }

    @Provides
    fun providesStockDao(database: StockDatabase) = database.stockDao()
}