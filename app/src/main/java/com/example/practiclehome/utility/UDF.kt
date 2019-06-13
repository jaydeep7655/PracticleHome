package com.example.practiclehome.utility

import android.content.Context
import android.net.ConnectivityManager
/**
 * Created by jaydeep.
 */

object UDF {
    /**
     * To check for internet availability status
     *
     * @param context the Context to access System Service
     * @return a boolean value (TRUE or FALSE)
     */
    fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context
                .CONNECTIVITY_SERVICE) as ConnectivityManager
        var result = false
        try {
            result = cm.activeNetworkInfo != null && cm.activeNetworkInfo
                .isAvailable && cm.activeNetworkInfo.isConnected
        } catch (e: Exception) {

        }

        return result
    }







}