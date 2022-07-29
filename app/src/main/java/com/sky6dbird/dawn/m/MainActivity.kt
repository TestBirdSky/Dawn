package com.sky6dbird.dawn.m

import android.animation.ObjectAnimator
import android.content.Intent
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.github.shadowsocks.bg.BaseService
import com.sky6dbird.dawn.App6D
import com.sky6dbird.dawn.R
import com.sky6dbird.dawn.activity6d.PrivacyActivity6D
import com.sky6dbird.dawn.activity6d.Result6DActivity
import com.sky6dbird.dawn.activity6d.Ser6DActivity
import com.sky6dbird.dawn.base6d.Base6DActivity
import com.sky6dbird.dawn.common6d.*
import com.sky6dbird.dawn.databinding.ActivityMainBinding

class MainActivity : Base6DActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val vConnectionManger by lazy { VConnectionManger(this) }
    private val mViewModel by lazy { ViewModelProvider(this).get<MainView6dModel>() }
    private val vpPermission6d = registerForActivityResult(VPermissionCheck()) {
        if (!it) {
            toggle6d()
        }
    }
    private val animatorConnecting by lazy {
        ObjectAnimator.ofInt(dataBinding.probar, "progress", 8, 100).apply {
            duration = 3000
            interpolator = LinearInterpolator()
        }
    }

    private val animatorDisconnecting by lazy {
        ObjectAnimator.ofInt(dataBinding.probar, "progress", 100, 8).apply {
            duration = 3000
            interpolator = LinearInterpolator()
        }
    }

    private var curState = BaseService.State.Idle
    override fun init() {
        vConnectionManger.stateCallback = {
            stateChange(it)
        }
        vConnectionManger.connect()
        dataBinding.run {
            ivSer.setOnClickListener {
                toNexAct6D(Ser6DActivity::class.java, requestCode = 1000)
            }
            tvStatus.setOnClickListener { vpPermission6d.launch(null) }
            ivSetting.setOnClickListener {
                drawer.open()
            }
            settingLayout.run {
                tvContactUs.setOnClickListener {
                    jumpEApp(Constants6d.EMAIL_6D)
                    drawer.close()
                }
                tvPrivacy.setOnClickListener {
                    toNexAct6D(PrivacyActivity6D::class.java)
                    drawer.close()
                }
                tvShare.setOnClickListener {
                    shareAppDownPath6D()
                    drawer.close()
                }
                tvUpdate.setOnClickListener {
                    jumpGooglePlay6d()
                    drawer.close()
                }
            }
        }
        mViewModel.run {
            con.observe(this@MainActivity) {
                dataBinding.ivCon.setImageResource(Utils6D.getNIconByName(it))
            }
            time.observe(this@MainActivity) {
                dataBinding.tvTime.text = it
            }
        }
    }

    override fun onStart() {
        super.onStart()
        vConnectionManger.start()
    }

    override fun onStop() {
        super.onStop()
        vConnectionManger.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        vConnectionManger.destroy()
    }

    override fun onPause() {
        super.onPause()
        if (!App6D.isAppResume) {
            mViewModel.activityOnPause()
            if (curState == BaseService.State.Connecting) {
                stateChange(BaseService.State.Stopped)
            } else if (curState == BaseService.State.Stopping) {
                stateChange(BaseService.State.Connected)
            }
        }
    }

    private fun stateChange(state: BaseService.State) {
        mViewModel.stateChange(state)
        when (state) {
            BaseService.State.Idle -> {
                showStoppedUi()
            }
            BaseService.State.Connecting -> showConnectingUi()
            BaseService.State.Connected -> {
                if (curState == BaseService.State.Connecting && isResume) {
                    toNexAct6D(Result6DActivity::class.java)
                }
                showConnectedUi()
            }
            BaseService.State.Stopping -> showStoppingUi()
            BaseService.State.Stopped -> {
                if (curState == BaseService.State.Stopping && isResume) {
                    toNexAct6D(Result6DActivity::class.java)

                }
                showStoppedUi()
            }
        }
        curState = state
    }


    private fun showStoppedUi() {
        dataBinding.run {
            tvStatus.text = "Connect"
            homeparent.setBackgroundResource(R.drawable.ic_home6d_bg1)
            animatorDisconnecting.end()
            animatorConnecting.end()
            probar.progress = 0
        }
    }

    private fun showStoppingUi() {
        dataBinding.run {
            tvStatus.text = "Disconnecting..."
            homeparent.setBackgroundResource(R.drawable.ic_home_bg3)
            animatorDisconnecting.start()
            animatorConnecting.end()
        }
    }

    private fun showConnectingUi() {
        dataBinding.run {
            tvStatus.text = "Connecting..."
            homeparent.setBackgroundResource(R.drawable.ic_hoem6d_bg2)
            animatorDisconnecting.end()
            animatorConnecting.start()
        }
    }

    private fun showConnectedUi() {
        dataBinding.run {
            tvStatus.text = "Connected"
            homeparent.setBackgroundResource(R.drawable.ic_home_bg3)
            animatorDisconnecting.end()
            animatorConnecting.end()
            probar.progress = 100
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                data?.let {
                    val name = it.getStringExtra("name")
                    val con = it.getStringExtra("con")
                    name?.let { na ->
                        mViewModel.nameSer = na
                    }
                    con?.let { c ->
                        mViewModel.con.value = c
                    }
                    vpPermission6d.launch(null)
                }
            }
        }
    }

    private fun toggle6d() {
        if (Utils6D.hasNetwork()) {
            val isStopped = curState == BaseService.State.Stopped
            stateChange(if (isStopped) BaseService.State.Connecting else BaseService.State.Stopping)
        } else {
            Toast.makeText(this, "Sorry, your phone has no network", Toast.LENGTH_SHORT).show()
        }
    }
}