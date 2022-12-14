package com.sky6dbird.dawn

import android.app.ActivityManager
import android.app.Application
import android.os.Process
import com.github.shadowsocks.Core
import com.sky6dbird.dawn.activity6d.Splash6DActivity

/**
 * Date：2022/7/28
 * Describe:
 */
class App6D :Application() {
    companion object {
        lateinit var mApp: App6D
        var isAppResume = false
    }
    var isVConnected = false
    var isRefreshHomeNativeAd = false
    var resultCon = ""
    var connectedTime = -1L

    override fun onCreate() {
        super.onCreate()
        Core.init(this, Splash6DActivity::class)
        if (!isBgP()) {
            mApp = this
            registerActivityLifecycleCallbacks(ACL6D())
        }
    }

    private fun isBgP(): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (processInfo in manager.runningAppProcesses) {
            if (processInfo.pid == Process.myPid()) {
                return processInfo.processName.contains(":bg")
            }
        }
        return false
    }
}