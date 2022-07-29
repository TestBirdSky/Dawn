package com.sky6dbird.dawn.cache6d

import com.google.gson.Gson
import com.sky6dbird.dawn.SerUi6DData
import com.sky6dbird.dawn.bean6d.FastBean6d
import com.sky6dbird.dawn.bean6d.SerBean6d
import com.sky6dbird.dawn.bean6d.ServerListBean6d
import com.sky6dbird.dawn.common6d.Constants6d

/**
 * Date：2022/7/29
 * Describe:
 */
object AppCache {
    private val fastBean3O by lazy {
        Gson().fromJson(
            Constants6d.localFastCity6DStr,
            FastBean6d::class.java
        )
    }
    private val serListBean3O by lazy {
        Gson().fromJson(
            Constants6d.localSerList6DStr,
            ServerListBean6d::class.java
        )
    }

    fun getSerList(): ArrayList<SerBean6d> {
        return serListBean3O.d_6d_s
    }

    private fun getFastList(): ArrayList<String> {
        return fastBean3O.dawn_f
    }

    fun getSerUiBeanList(): ArrayList<SerUi6DData> {
        val data = arrayListOf<SerUi6DData>()
        data.add(SerUi6DData(Constants6d.DEFAULT_SERVER_NAME, Constants6d.DEFAULT_SERVER_NAME))
        for (ser in getSerList()) {
            data.add(SerUi6DData(ser.getName6D(), ser.d_6d_con))
        }
        return data
    }

    fun getFastSerBeanList(): ArrayList<SerBean6d> {
        val data = arrayListOf<SerBean6d>()
        for (s in getFastList()) {
            for (ser in getSerList()) {
                if (s == ser.d_6d_ci) {
                    data.add(ser)
                    break
                }
            }
        }
        return data
    }
}