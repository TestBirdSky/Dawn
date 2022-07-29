package com.sky6dbird.dawn

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.sky6dbird.dawn.App6D.Companion.isAppResume
import com.sky6dbird.dawn.activity6d.Splash6DActivity
import com.sky6dbird.dawn.common6d.Log6D
import com.sky6dbird.dawn.common6d.toNexAct6D
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Date：2022/7/29
 * Describe:
 */
class ACL6D: Application.ActivityLifecycleCallbacks {
    private var leaveAppTime = -1L
    private var pageResumeTagNum = 0
    private val mApp by lazy { App6D.mApp }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Log6D.i("onActivityCreated $activity")
    }

    override fun onActivityStarted(activity: Activity) {
        Log6D.i("onActivityStarted $activity")
        if (leaveAppTime != -1L && System.currentTimeMillis() - leaveAppTime >= 3000L) {
            leaveAppTime = -1L
            mApp.isRefreshHomeNativeAd = true
            if (activity is Splash6DActivity) {

            } else {
                activity.toNexAct6D(Splash6DActivity::class.java, Bundle().apply {
                })
            }
        }
        add()
    }

    override fun onActivityResumed(activity: Activity) {
        Log6D.i("onActivityResumed $activity")
        add()
    }

    override fun onActivityPaused(activity: Activity) {
        Log6D.i("onActivityPaused $activity")
        reduce()
    }

    override fun onActivityStopped(activity: Activity) {
        Log6D.i("onActivityStopped $activity")
        reduce()
//        if (activity is AdActivity) {
//            CoroutineScope(Dispatchers.Main).launch {
//                delay(2500L)
//                if (leaveAppTime != -1L && (!isAppResume) && !activity.isFinishing) {
//                    Log6D.i("onActivityStopped finish-->$activity")
//                    activity.finish()
//                }
//            }
//        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        Log6D.i("onActivitySaveInstanceState $activity")
    }

    override fun onActivityDestroyed(activity: Activity) {
        Log6D.i("onActivityDestroyed $activity")
    }

    private fun add() {
        isAppResume = true
        leaveAppTime = -1L
        pageResumeTagNum++
    }

    private fun reduce() {
        pageResumeTagNum--
        if (pageResumeTagNum <= 0) {
            isAppResume = false
            leaveAppTime = System.currentTimeMillis()
        }
    }
}