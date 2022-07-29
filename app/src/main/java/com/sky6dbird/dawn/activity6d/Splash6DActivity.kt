package com.sky6dbird.dawn.activity6d

import android.animation.ObjectAnimator
import android.view.animation.LinearInterpolator
import androidx.lifecycle.lifecycleScope
import com.sky6dbird.dawn.App6D
import com.sky6dbird.dawn.m.MainActivity
import com.sky6dbird.dawn.R
import com.sky6dbird.dawn.base6d.Base6DActivity
import com.sky6dbird.dawn.common6d.Utils6D
import com.sky6dbird.dawn.common6d.toNexAct6D
import com.sky6dbird.dawn.databinding.Activity6dsplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Date：2022/7/29
 * Describe:
 */
class Splash6DActivity : Base6DActivity<Activity6dsplashBinding>(R.layout.activity_6dsplash) {

    private val objectAnimator by lazy {
        ObjectAnimator.ofInt(dataBinding.probar, "progress", 0, 100).apply {
            duration = 3000
            interpolator = LinearInterpolator()
        }
    }

    private var startTime = 0L
    override fun init() {
        startTime = System.currentTimeMillis()
        objectAnimator.start()
        lifecycleScope.launch {
            if (Utils6D.hasNetwork()) {
                delay(1000)
                var leavePageTime = 0L
                while (System.currentTimeMillis() - startTime < 3000) {
                    if (isResume) {

                    } else if (leavePageTime == 0L) {
                        leavePageTime = System.currentTimeMillis()
                    } else if (System.currentTimeMillis() - leavePageTime > 2500) {
                        break
                    }
                    delay(300)
                }
                toMainPage2()
            } else {
                delay(1000)
                toMainPage2()
            }
        }
    }

    private fun toMainPage2() {
        if (App6D.isAppResume) {
            toNexAct6D(MainActivity::class.java)
        }
        finish()
    }

    override fun onBackPressed() {

    }
}