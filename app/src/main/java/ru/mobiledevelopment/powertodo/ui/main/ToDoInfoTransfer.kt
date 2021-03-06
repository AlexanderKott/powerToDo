package ru.mobiledevelopment.powertodo.ui.main

import com.google.gson.Gson
import com.google.gson.GsonBuilder


data class ToDoInfoTransfer(val id: Long, val lines: String, val multiLine: Boolean)

object Utils {
    private var gson: Gson? = null
    val gsonParser: Gson?
         get() {
            if (null == gson) {
                val builder = GsonBuilder()
                gson = builder.create()
            }
            return gson
        }
}