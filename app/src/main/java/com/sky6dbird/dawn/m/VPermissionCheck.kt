package com.sky6dbird.dawn.m

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.VpnService
import androidx.activity.result.contract.ActivityResultContract
import com.github.shadowsocks.preference.DataStore
import com.github.shadowsocks.utils.Key

/**
 * Date：2022/7/29
 * Describe:
 */
class VPermissionCheck:ActivityResultContract<Void?,Boolean>() {
    private var cachedIntent: Intent? = null

    override fun getSynchronousResult(
        context: Context,
        input: Void?
    ): SynchronousResult<Boolean>? {
        if (DataStore.serviceMode == Key.modeVpn) VpnService.prepare(context)?.let { intent ->
            cachedIntent = intent
            return null
        }
        return SynchronousResult(false)
    }

    override fun createIntent(context: Context, input: Void?) =
        cachedIntent!!.also { cachedIntent = null }

    override fun parseResult(resultCode: Int, intent: Intent?) =
        resultCode != Activity.RESULT_OK
}