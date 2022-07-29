package com.sky6dbird.dawn

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sky6dbird.dawn.cache6d.AppCache
import com.sky6dbird.dawn.cache6d.getSeName6d
import com.sky6dbird.dawn.common6d.Utils6D
import com.sky6dbird.dawn.databinding.ItemLayoutBinding

/**
 * Date：2022/7/29
 * Describe:
 */
class SerAdapter : RecyclerView.Adapter<SerAdapter.MyHolder>() {
    private val data6d = AppCache.getSerUiBeanList()
    var curSelectName = getSeName6d()
    var itemClick: (serUi6DData: SerUi6DData) -> Unit = {}

    inner class MyHolder(private val db: ItemLayoutBinding) : RecyclerView.ViewHolder(db.root) {
        fun bind6d(serUi6DData: SerUi6DData) {
            db.run {
                item.setBackgroundResource(if (curSelectName == serUi6DData.name) R.color.green_60A346 else R.color.white_18)
                tvName.text = serUi6DData.name
                ivNative.setImageResource(Utils6D.getNIconByName(serUi6DData.con))
                item.setOnClickListener {
                    curSelectName = serUi6DData.name
                    notifyDataSetChanged()
                    itemClick.invoke(serUi6DData)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind6d(data6d[position])
    }

    override fun getItemCount() = data6d.size

}

data class SerUi6DData(val name: String, val con: String)