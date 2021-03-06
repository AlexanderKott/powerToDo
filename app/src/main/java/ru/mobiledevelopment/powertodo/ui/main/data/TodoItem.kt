package ru.mobiledevelopment.powertodo.ui.main.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "todos_table")
data class TodoItem(
    @PrimaryKey(autoGenerate = true)
    var number: Long = 0,
    @ColumnInfo(typeAffinity = ColumnInfo.TEXT)
    var text: String? = "",
    var date: Long = 0,
    var done: Boolean = false,
    @Ignore
    var color: Int = 0
)

