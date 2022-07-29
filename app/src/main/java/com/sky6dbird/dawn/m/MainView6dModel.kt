package com.sky6dbird.dawn.m

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.shadowsocks.Core
import com.github.shadowsocks.bg.BaseService
import com.github.shadowsocks.database.Profile
import com.github.shadowsocks.database.ProfileManager
import com.github.shadowsocks.preference.DataStore
import com.sky6dbird.dawn.App6D
import com.sky6dbird.dawn.R
import com.sky6dbird.dawn.bean6d.SerBean6d
import com.sky6dbird.dawn.cache6d.*
import com.sky6dbird.dawn.common6d.Constants6d
import com.sky6dbird.dawn.common6d.Log6D
import com.sky6dbird.dawn.common6d.Utils6D
import kotlinx.coroutines.*
import java.util.*

/**
 * Date：2022/7/29
 * Describe:
 */
class MainView6dModel : ViewModel() {
    var nameSer = getSeName6d()
    val con = MutableLiveData(getCon())
    val time = MutableLiveData("00:00:00")
    private var isConnected = false
    private var mState = BaseService.State.Idle
    private val mApp by lazy { App6D.mApp }
    fun stateChange(state: BaseService.State) {
        Log6D.i("viewmodel ---> $state")
        when (state) {
            BaseService.State.Idle -> {
                // do nothing
            }
            BaseService.State.Stopped -> stoppedV()
            BaseService.State.Connecting -> connecting()
            BaseService.State.Stopping -> stopping()
            BaseService.State.Connected -> {
                if (mState == BaseService.State.Connecting) {
                    mApp.connectedTime = System.currentTimeMillis()
                }
                connectedV()
            }
        }
        mState = state
    }

    private var vJob: Job? = null
    var showConnectionAdListener: () -> Unit = {}
    private fun connecting() {
        vJob?.cancel()
        vJob = viewModelScope.launch {
            val time = System.currentTimeMillis()
//            loadAd()
            selectedProfile()
            checkAdLoadFinish(time)
            Core.startService()
        }
    }

    private fun stopping() {
        vJob = viewModelScope.launch {
//            loadAd()
            checkAdLoadFinish(System.currentTimeMillis())
            Core.stopService()
        }
    }

    private suspend fun checkAdLoadFinish(time: Long) {
        withContext(Dispatchers.Default) {
            if (System.currentTimeMillis() - time < 2000) {
                delay(2000 - (System.currentTimeMillis() - time))
            }
            while (System.currentTimeMillis() - time < 2000) {
//                if (AdSpace5B.Connection.ad5BWrap.adData != null) {
//                    break
//                }
                delay(500)
            }
            withContext(Dispatchers.Main) {
                showConnectionAdListener.invoke()
            }
        }
    }

    //
//    private fun loadAd() {
//        Ad5BManage.loadAd(AdSpace5B.Connection)
//        Ad5BManage.loadAd(AdSpace5B.Result)
//    }
//
    private fun connectedV() {
        isConnected = true
        mApp.isVConnected = true
        mApp.resultCon = con.value!!
        viewModelScope.launch {
            putSerName(nameSer)
            putContory(mApp.resultCon)
            var count = (System.currentTimeMillis() - mApp.connectedTime) / 1000
            while (isConnected) {
                time.value = Utils6D.intTimeToStr(count)
                count++
                delay(1000)
            }
        }
    }

    private fun stoppedV() {
        isConnected = false
        mApp.isVConnected = false
        putSerName(nameSer)
        putContory(con.value!!)
        time.value = Utils6D.intTimeToStr(0)
    }

    fun activityOnPause() {
        vJob?.cancel()
        if (mState == BaseService.State.Connecting || mState == BaseService.State.Stopping) {
            nameSer = getSeName6d()
            con.value = getCon()
        }
    }


    private suspend fun selectedProfile() {
        withContext(Dispatchers.Default) {
            if (nameSer!= Constants6d.DEFAULT_SERVER_NAME) {
                for (ser in AppCache.getSerList()) {
                    if (ser.getName6D() == nameSer) {
                        updateProfile(ser)
                        break
                    }
                }
            } else {
                val list = AppCache.getFastSerBeanList()
                val size = list.size
                if (size == 0) {
                    Log6D.e("no data")
                    return@withContext
                }
                val beans = arrayListOf<SerBean6d>()
                val time = System.currentTimeMillis()
                val jobs = arrayListOf<Job>()
                for (i: Int in 0 until list.size) {
                    jobs.add(viewModelScope.launch(Dispatchers.IO) {
                        val delay = Utils6D.delayTest(list[i].d_6d_ip)
                        Log6D.i("${list[i].d_6d_ip}----delay=$delay")
                        val serBean: SerBean6d = list[i]
                        serBean.delay = delay
                        beans.add(serBean)
                    })
                }
                withContext(Dispatchers.Default) {
                    while (beans.size < list.size && System.currentTimeMillis() - time < 2000) {
                        delay(200)
                    }
                }
                jobs.forEach { it.cancel() }
                Log6D.i("size->${beans.size}")
                beans.sortBy { it.delay }
                val fastBeans = arrayListOf<SerBean6d>()
                for (i: Int in 0 until beans.size) {
                    if (i < 3) {
                        fastBeans.add(beans[i])
                    } else {
                        break
                    }
                }
                val sizeFb = fastBeans.size
                var randomIndex = 0
                if (sizeFb > 1) {
                    randomIndex = Random().nextInt(sizeFb)
                }
                val fastBean = fastBeans[randomIndex]
                updateProfile(fastBean)
            }
        }
    }

    private fun updateProfile(server: SerBean6d) {
        var profile = ProfileManager.getProfile(DataStore.profileId)
        if (profile == null) {
            profile = ProfileManager.createProfile(Profile())
            profile.name = mApp.getString(R.string.app_name)
            DataStore.profileId = profile.id
        }
        Log6D.i("switchProfile: $server  --$profile  --${DataStore.profileId}")
        profile.host = server.d_6d_ip
        profile.remotePort = server.d_6d_pt
        profile.password = server.d_6d_pwd
        profile.method = server.d_6d_m
        ProfileManager.updateProfile(profile)
    }
}