package com.sky6dbird.dawn.base6d

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.sky6dbird.dawn.App6D
import com.sky6dbird.dawn.common6d.setDensity

/**
 * Date：2022/7/28
 * Describe:
 */
abstract class Base6DActivity<db : ViewDataBinding>(private val layoutId: Int) : AppCompatActivity() {
    protected val mApp by lazy { App6D.mApp }
    protected lateinit var dataBinding: db
    protected var isResume = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDensity()
        dataBinding = DataBindingUtil.setContentView(this, layoutId)
        dataBinding.lifecycleOwner = this
        init()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    abstract fun init()

    override fun onResume() {
        super.onResume()
        isResume = true
    }

    override fun onPause() {
        super.onPause()
        isResume = false
    }
}