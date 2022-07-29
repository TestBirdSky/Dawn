package com.sky6dbird.dawn.m

import android.content.Context
import android.os.RemoteException
import com.github.shadowsocks.aidl.IShadowsocksService
import com.github.shadowsocks.aidl.ShadowsocksConnection
import com.github.shadowsocks.bg.BaseService

/**
 * Date：2022/7/29
 * Describe:
 */
class VConnectionManger(private val context: Context) : ShadowsocksConnection.Callback {


    private val sc = ShadowsocksConnection(true)
    var stateCallback: (state: BaseService.State) -> Unit = {}

    fun connect() {
        sc.connect(context, this)
    }

    fun start() {
        sc.bandwidthTimeout = 500
    }

    fun stop() {
        sc.bandwidthTimeout = 0
    }


    fun destroy() {
        sc.disconnect(context)
    }

    override fun stateChanged(state: BaseService.State, profileName: String?, msg: String?) {
        stateCallback.invoke(state)
    }

    override fun onServiceConnected(service: IShadowsocksService) {
        try {
            stateCallback.invoke(BaseService.State.values()[service.state])
        } catch (_: RemoteException) {
            stateCallback.invoke(BaseService.State.Idle)
        }
    }

    override fun onBinderDied() {
        sc.disconnect(context)
        sc.connect(context, this)
    }

    override fun onServiceDisconnected() = stateCallback.invoke(BaseService.State.Idle)
}