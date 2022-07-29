package com.sky6dbird.dawn.bean6d

/**
 * Date：2022/7/29
 * Describe:
 */
data class SerBean6d(
    val d_6d_ip: String,
    val d_6d_pwd: String,
    val d_6d_pt: Int,
    val d_6d_m: String,
    val d_6d_con: String,
    val d_6d_ci: String,
) {
    var delay = Int.MAX_VALUE
    fun getName6D(): String {
        return "$d_6d_con-$d_6d_ci"
    }
}
