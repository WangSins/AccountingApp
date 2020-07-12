package me.sin.accountingapp.base

import android.app.Application
import android.content.Context

/**
 * Created by Sin on 2020/7/12
 */

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    companion object {
        var context: Context? = null
            private set
    }
}