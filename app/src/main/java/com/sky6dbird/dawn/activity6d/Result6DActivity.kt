package com.sky6dbird.dawn.activity6d

import androidx.lifecycle.lifecycleScope
import com.sky6dbird.dawn.App6D
import com.sky6dbird.dawn.R
import com.sky6dbird.dawn.base6d.Base6DActivity
import com.sky6dbird.dawn.common6d.Utils6D
import com.sky6dbird.dawn.databinding.LayoutResult6dActivityBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Date：2022/7/29
 * Describe:
 */
class Result6DActivity :
    Base6DActivity<LayoutResult6dActivityBinding>(R.layout.layout_result_6d_activity) {
    override fun init() {
        dataBinding.run {
            ivBack.setOnClickListener { onBackPressed() }
            var count = ( System.currentTimeMillis()-mApp.connectedTime) / 1000
            tvTime.text = Utils6D.intTimeToStr(count)
            ivCon.setImageResource(Utils6D.getNIconByName(mApp.resultCon))
            if (mApp.isVConnected) {
                lifecycleScope.launch {
                    while (!isFinishing) {
                        delay(1000)
                        count++
                        tvTime.text = Utils6D.intTimeToStr(count)
                    }
                }
            } else {
                ivConBg.setBackgroundResource(R.drawable.result_6d_con_bg2)
                ivCon.setBackgroundResource(R.drawable.result_6d_con_bg_dis)
                tvTime.setTextColor(getColor(R.color.red_DF3634))
                tvStatus.setTextColor(getColor(R.color.red_DF3634))
                tvStatus.text = "Disconnection succeed"
                ivCon.setImageResource(Utils6D.getNIconByName(mApp.resultCon))
            }
        }

    }
}