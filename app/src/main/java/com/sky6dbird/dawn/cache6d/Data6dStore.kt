package com.sky6dbird.dawn.cache6d

import android.content.Context
import android.text.TextUtils
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sky6dbird.dawn.App6D
import com.sky6dbird.dawn.common6d.Constants6d.DEFAULT_SERVER_NAME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Date：2022/7/29
 * Describe:
 */
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "Dawn")

private val KEY16D = stringPreferencesKey("selectName6D")
private val KEY26D = stringPreferencesKey("contory6d")
private val KEY33O = stringPreferencesKey("adConfigure")
private val mApp by lazy { App6D.mApp }

fun putSerName(name: String) {
    CoroutineScope(Dispatchers.IO).launch {
        mApp.dataStore.edit { it[KEY16D] = name }
    }
}

fun getSeName6d(): String {
    return runBlocking {
        mApp.dataStore.data.map {
            if (TextUtils.isEmpty(it[KEY16D])) {
                DEFAULT_SERVER_NAME
            } else {
                it[KEY16D] ?: DEFAULT_SERVER_NAME
            }
        }.first()
    }
}

fun putContory(con: String) {
    CoroutineScope(Dispatchers.IO).launch {
        mApp.dataStore.edit { it[KEY26D] = con }
    }
}

fun getCon() = runBlocking {
    mApp.dataStore.data.map {
        if (TextUtils.isEmpty(it[KEY26D])) {
            DEFAULT_SERVER_NAME
        } else {
            it[KEY26D] ?: DEFAULT_SERVER_NAME
        }
    }.first()
}

//fun putAdConfigure(ad: String) {
//    CoroutineScope(Dispatchers.IO).launch {
//        mApp.dataStore.edit { it[KEY33O] = ad }
//    }
//}
//
//fun getAdConfigure() = runBlocking {
//    mApp.dataStore.data.map {
//        it[KEY33O] ?: Constants3O.localConfigure
//    }.first()
//}