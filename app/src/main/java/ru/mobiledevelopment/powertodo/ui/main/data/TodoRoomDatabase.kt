package ru.mobiledevelopment.powertodo.ui.main.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TodoItem::class], version = 2)
abstract class TodoRoomDatabase : RoomDatabase() {
    abstract fun todoListTableDAO(): TodoListTableDAO
}