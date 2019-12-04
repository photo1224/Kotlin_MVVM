package com.crazy.kotlin_mvvm

import android.app.Application
import android.content.Context
import kotlin.properties.Delegates


/**
 * Created by wtc on 2019/11/1
 */
open class BaseApplication : Application() {

    companion object{
        var context : Context by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()

        context = applicationContext
    }
}