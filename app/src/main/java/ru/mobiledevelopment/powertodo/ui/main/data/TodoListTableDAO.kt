package ru.mobiledevelopment.powertodo.ui.main.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TodoListTableDAO {
    @Query("SELECT * FROM todos_table ORDER BY number DESC")
    fun getAllRecords():  LiveData<List<TodoItem>>


    @Insert
    fun insert(t: TodoItem)

    @Insert
    fun insert(t: List<TodoItem>): List<Long>

    @Update
    fun update(t: TodoItem)

    @Delete
    fun delete(t: TodoItem)

    @Query("DELETE FROM todos_table WHERE number = :userId")
    fun delete(userId: Long)
}