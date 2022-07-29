package com.sky6dbird.dawn.common6d

import android.util.Log
import com.sky6dbird.dawn.BuildConfig

/**
 * Date：2022/7/29
 * Describe:
 */
object Log6D {
    private const val TAG = "OrangeLog"

    fun i(msg: String) {
        i(TAG, msg)
    }

    fun e(msg: String) {
        e(TAG, msg)
    }

    fun i(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, msg)
        }
    }

    fun e(tag: String = TAG, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg)
        }
    }
}

const val AD_TAG = "OrangeAdLog"
fun adLogE(msg: String) {
    Log6D.e(AD_TAG, msg)
}

fun adLogI(msg: String) {
    Log6D.i(AD_TAG, msg)
}