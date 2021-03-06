package ru.mobiledevelopment.powertodo.ui.main

import android.app.Application
import androidx.room.Room
import ru.mobiledevelopment.powertodo.ui.main.data.TodoRoomDatabase

class App : Application() {
    private lateinit var database: TodoRoomDatabase

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = Room.databaseBuilder(this, TodoRoomDatabase::class.java, "database.db")
            .fallbackToDestructiveMigration().build()
    }

    fun getDatabase(): TodoRoomDatabase {
        return database
    }

    companion object {
        lateinit var instance: App

        @JvmStatic
        fun getApp(): App {
            return instance
        }
    }
}