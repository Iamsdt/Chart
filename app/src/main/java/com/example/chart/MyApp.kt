package com.example.chart

import android.app.Application
import com.rohitss.uceh.UCEHandler

class MyApp:Application(){

    override fun onCreate() {
        super.onCreate()
        UCEHandler.Builder(this).build()
    }

}