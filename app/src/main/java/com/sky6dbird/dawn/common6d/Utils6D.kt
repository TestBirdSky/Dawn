package com.sky6dbird.dawn.common6d

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.sky6dbird.dawn.App6D
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder
import kotlin.math.roundToInt

/**
 * Date：2022/7/28
 * Describe:
 */
object Utils6D {
    private val mApp by lazy { App6D.mApp}

    fun getPackInfo(): PackageInfo {
        val pm = mApp.packageManager
        val packageInfo =
            pm.getPackageInfo(mApp.packageName, PackageManager.GET_ACTIVITIES)
        return packageInfo
    }

    fun hasNetwork(): Boolean {
        val cm = mApp
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        //如果仅仅是用来判断网络连接 //则可以使用 cm.getActiveNetworkInfo().isAvailable();
        val info = cm.allNetworkInfo
        for (i in info.indices) {
            if (info[i].state == NetworkInfo.State.CONNECTED) {
                return true
            }
        }
        return false
    }

//    fun getNIconByName(nation: String?): Int {
//        return when (nation) {
////            Cons5btants.NATION_GERMANY -> R.drawable.ic_n_circle_germany
////            Cons5btants.NATION_UNITEDKINGDOM -> R.drawable.ic_n_circleunited_kingdom
////            Cons5btants.NATION_UNITEDSTATES -> R.drawable.ic_n_circle_unitedstates
////            Cons5btants.NATION_JAPAN -> R.drawable.ic_n_circle_japan
////            Cons5btants.NATION_FRANCE -> R.drawable.ic_n_circle_france
////            else -> R.drawable.ic_default_native
//        }
//    }


    fun intTimeToStr(time: Long): String {
        return "${String.format("%02d", time / 3600)}:${
            String.format(
                "%02d",
                time % 3600 / 60
            )
        }:${String.format("%02d", time % 60)}"
    }

    suspend fun delayTest(ip: String): Int {
        var delay = Int.MAX_VALUE
        val count = 1
        val timeout = 1
        val cmd = "/system/bin/ping -c $count -w $timeout $ip"
        return withContext(Dispatchers.IO) {
            val r = ping(cmd)
            Log6D.i("$r")
            if (r != null) {
                try {
                    val index: Int = r.indexOf("min/avg/max/mdev")
                    if (index != -1) {
                        val tempInfo: String = r.substring(index + 19)
                        val temps = tempInfo.split("/".toRegex()).toTypedArray()
                        delay = temps[0].toFloat().roundToInt()//min
                    }
                } catch (e: Exception) {

                }
            }
            delay
        }
    }

    private fun ping(cmd: String): String? {
        var process: Process? = null
        try {
            process = Runtime.getRuntime().exec(cmd) //执行ping指令
            val inputStream = process!!.inputStream
            val reader = BufferedReader(InputStreamReader(inputStream))
            val sb = StringBuilder()
            var line: String?
            while (null != reader.readLine().also { line = it }) {
                sb.append(line)
                sb.append("\n")
            }
            reader.close()
            inputStream.close()
            return sb.toString()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            process?.destroy()
        }
        return null
    }
}