package ru.mobiledevelopment.powertodo.ui.main.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.mobiledevelopment.powertodo.ui.main.data.TodoRoomDatabase


internal class ModelFactory(private val base: TodoRoomDatabase) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T{
        if (modelClass.isAssignableFrom(ToDoViewModel::class.java)) {
            return ToDoViewModel(base) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

