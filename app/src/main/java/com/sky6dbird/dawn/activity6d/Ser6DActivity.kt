package com.sky6dbird.dawn.activity6d

import android.app.AlertDialog
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.sky6dbird.dawn.R
import com.sky6dbird.dawn.SerAdapter
import com.sky6dbird.dawn.SerUi6DData
import com.sky6dbird.dawn.base6d.Base6DActivity
import com.sky6dbird.dawn.cache6d.getSeName6d
import com.sky6dbird.dawn.databinding.ActivityLocationBinding

/**
 * Date：2022/7/29
 * Describe:
 */
class Ser6DActivity : Base6DActivity<ActivityLocationBinding>(R.layout.activity_location) {
    private lateinit var adapter: SerAdapter
    override fun init() {
        adapter = SerAdapter().apply {
            itemClick = { clickItem(it) }
        }
        dataBinding.run {
            ivBack.setOnClickListener { onBackPressed() }
            recyclerView.layoutManager = LinearLayoutManager(this@Ser6DActivity)
            recyclerView.adapter = adapter
        }
    }

    private fun clickItem(serUi6DData: SerUi6DData) {
        if (mApp.isVConnected) {
            if (serUi6DData.name != getSeName6d()) {
                val dialog = AlertDialog.Builder(this)
                    .setOnCancelListener {
                        adapter.curSelectName = getSeName6d()
                        adapter.notifyDataSetChanged()
                    }
                    .setMessage("You are currently connected to another server. Would you like to disconnect the current connection for you?")
                    .setPositiveButton("OK") { dialog, which ->
                        dialog.dismiss()
                        setResult(RESULT_OK, Intent().apply {
                            putExtra("name", serUi6DData.name)
                            putExtra("con", serUi6DData.con)
                        })
                        finish()
                    }.create()
                dialog.show()
            }
        } else {
            setResult(RESULT_OK, Intent().apply {
                putExtra("name", serUi6DData.name)
                putExtra("con", serUi6DData.con)
            })
            finish()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}