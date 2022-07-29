package com.sky6dbird.dawn.common6d

/**
 * Date：2022/7/29
 * Describe:
 */
object Constants6d {
    const val EMAIL_6D = "email"
    const val PRIVACY_6D = "www.333.com"

    const val DEFAULT_SERVER_NAME = "Fast Server"
    const val NATION_GERMANY = "Germany"//德国
    const val NATION_UNITEDKINGDOM = "United Kingdom" //英国
    const val NATION_UNITEDSTATES = "United States"//美国
    const val NATION_JAPAN = "Japan"//日本
    const val NATION_FRANCE = "France"//法国
    const val NATION_CANADA = "Canada"//加拿大
    const val NATION_SINGAPORE = "Singapore"//新加坡

    const val localFastCity6DStr = """
        {
          "dawn_f": [
            "New York",
            "London",
            "Tokyo"
          ]
        }
   """
    const val localSerList6DStr = """
{
  "d_6d_s": [
    {
      "d_6d_ip": "192.132.3.1",
      "d_6d_pwd": "0qMKBzCliiICuu7t",
      "d_6d_pt": 1254,
      "d_6d_m": "chacha20-ietf-poly1305",
      "d_6d_con": "United States",
      "d_6d_ci": "New York"
    },
    {
      "d_6d_ip": "112.141.3.1",
      "d_6d_pwd": "0qMKBzCliiICuu7t",
      "d_6d_pt": 1254,
      "d_6d_m": "chacha20-ietf-poly1305",
      "d_6d_con": "United Kingdom",
      "d_6d_ci": "London"
    },
    {
      "d_6d_ip": "124.168.8.5",
      "d_6d_pwd": "0qMKBzCliiICuu7t",
      "d_6d_pt": 1254,
      "d_6d_m": "chacha20-ietf-poly1305",
      "d_6d_con": "Japan",
      "d_6d_ci": "Tokyo"
    }
  ]
}
"""
}