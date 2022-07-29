package com.sky6dbird.dawn.common6d

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Toast

/**
 * Date：2022/7/28
 * Describe:
 */
fun Activity.toNexAct6D(
    activity: Class<*>,
    bundle: Bundle? = null,
    requestCode: Int? = null
): Activity {
    val intent = Intent(this, activity)
    bundle?.let { intent.putExtras(it) }
    requestCode?.let {
        startActivityForResult(intent, requestCode)
    } ?: run {
        startActivity(intent)
    }
    return this
}

fun Activity.shareTextToOtherApp(msg: String) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(
        Intent.EXTRA_TEXT,
        msg
    )
    startActivity(Intent.createChooser(intent, "share"))
}

fun Activity.jumpEApp(ownerEmail: String) {
    val s = "mailto:${ownerEmail}"
    val intent = Intent(Intent.ACTION_SENDTO)
    intent.data = Uri.parse(s)
    if (isIntentAvailable(Intent.ACTION_SENDTO)) {
        startActivity(intent)
    } else {
        Toast.makeText(this, "Contact us email is $ownerEmail", Toast.LENGTH_LONG).show()
    }
}

fun Activity.isIntentAvailable(action: String): Boolean {
    val packageManager: PackageManager = packageManager
    val intent = Intent(action)
    val list = packageManager.queryIntentActivities(
        intent,
        PackageManager.MATCH_DEFAULT_ONLY
    )
    return list.size > 0
}

fun Activity.setDensity() {
    val metrics: DisplayMetrics = resources.displayMetrics
    val td = metrics.heightPixels / 760f
    val dpi = (160 * td).toInt()
    metrics.density = td
    metrics.scaledDensity = td
    metrics.densityDpi = dpi
}

fun Activity.getPackInfo(): PackageInfo {
    val pm = packageManager
    return pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
}