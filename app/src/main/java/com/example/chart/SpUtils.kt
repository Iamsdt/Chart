package com.example.chart

import android.content.Context
import android.content.SharedPreferences

class SpUtils(context: Context) {


    var savePass:String?
        get() = sp.getString("Pass", "")
        set(value) {
            val ed = sp.edit()
            ed.putString("Pass", value)
            ed.apply()
        }

    private val sp:SharedPreferences = context.getSharedPreferences("MYSp", Context.MODE_PRIVATE)

}