package com.example.myapplication.data

import android.content.Context
import com.google.gson.Gson
import java.io.InputStreamReader

object DataFactory {
    fun readFromAssets(context: Context, fileName: String): JRoot {
        val inputStream = context.assets.open(fileName)
        val json = InputStreamReader(inputStream).readText()
        return Gson().fromJson(json, JRoot::class.java)
    }
}