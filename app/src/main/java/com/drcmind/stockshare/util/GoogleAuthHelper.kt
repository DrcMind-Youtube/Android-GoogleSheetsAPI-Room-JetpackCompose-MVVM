package com.drcmind.stockshare.util

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.services.sheets.v4.SheetsScopes
import kotlinx.coroutines.tasks.await

class GoogleAuthHelper(private val context: Context) {
    fun requestSignIn() : Intent {
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(Scope(SheetsScopes.SPREADSHEETS))
            .build()
        return  GoogleSignIn.getClient(context, signInOptions).signInIntent
    }

    suspend fun signIn(intent: Intent) : GoogleAccountCredential{
        val account = GoogleSignIn.getSignedInAccountFromIntent(intent).await()
        val scopes = listOf(SheetsScopes.SPREADSHEETS)
        val credential = GoogleAccountCredential.usingOAuth2(context,scopes)
        credential.selectedAccount = account.account
        return credential
    }

    fun getSignedInUser() : GoogleAccountCredential?{
        val account = GoogleSignIn.getLastSignedInAccount(context)
        return if(account!=null){
            val scopes = listOf(SheetsScopes.SPREADSHEETS)
            val credential = GoogleAccountCredential.usingOAuth2(context,scopes)
            credential.selectedAccount = account.account
            credential
        }else null
    }
}