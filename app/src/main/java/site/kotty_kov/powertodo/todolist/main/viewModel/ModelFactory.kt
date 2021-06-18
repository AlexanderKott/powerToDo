package site.kotty_kov.powertodo.todolist.main.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import site.kotty_kov.powertodo.todolist.main.common.App
import site.kotty_kov.powertodo.todolist.main.data.db.TodoRoomDatabase


internal class ModelFactory(private val base: TodoRoomDatabase) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(SharedViewModelToDo::class.java)) {
            return SharedViewModelToDo(base, App.getApp()) as T
        }
        if (modelClass.isAssignableFrom(SharedViewModelNotePad::class.java)) {
            return SharedViewModelNotePad(base, App.getApp()) as T
        }
        if (modelClass.isAssignableFrom(SharedViewModelCommon::class.java)) {
            return SharedViewModelCommon(base, App.getApp()) as T
        }


        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

