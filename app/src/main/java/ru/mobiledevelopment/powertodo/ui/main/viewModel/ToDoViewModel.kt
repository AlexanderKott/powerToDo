package ru.mobiledevelopment.powertodo.ui.main.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.mobiledevelopment.powertodo.ui.main.ToDoInfoTransfer
import ru.mobiledevelopment.powertodo.ui.main.data.TodoItem
import ru.mobiledevelopment.powertodo.ui.main.data.TodoListTableDAO
import ru.mobiledevelopment.powertodo.ui.main.data.TodoRoomDatabase
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

internal class ToDoViewModel(base: TodoRoomDatabase) : ViewModel() {
    private val executor: ExecutorService = Executors.newSingleThreadExecutor()
    private var todoListDAO: TodoListTableDAO = base.todoListTableDAO()

    val count = MutableLiveData<Int>(0)
    var items : LiveData<List<TodoItem>>  =  todoListDAO.getAllRecords()





       fun addNewItems(itemz: ToDoInfoTransfer) {
        executor.submit { todoListDAO.insert(prepareToDoItems(itemz)) }
           items  =  todoListDAO.getAllRecords()

    }

   fun setItemsCount(iCount : Int){
       if (iCount > 0){ count.value = 0} else {count.value = 1}
   }

    fun deleteItem(item: TodoItem) {
        executor.submit { todoListDAO.delete(item) }
    }



    fun updateItem(item: ToDoInfoTransfer) {
        executor.submit {
            todoListDAO.update(prepareToDoItems(item)[0]) }
    }

    fun updateItem(item: TodoItem) {
        executor.submit { todoListDAO.update(item) }
    }



    private fun prepareToDoItems(transferObj: ToDoInfoTransfer): List<TodoItem> {
        return if (transferObj.multiLine) {
            val arr: List<String> = transferObj.lines.split("\n")

            val itemsToAdd: MutableList<TodoItem> = ArrayList<TodoItem>()
            for (i in arr.indices.reversed()) {
                if (arr[i].trim() == "") {
                    continue
                }
                val it = TodoItem(text = arr[i], date = System.currentTimeMillis()  )
                itemsToAdd.add(it)
            }
            itemsToAdd
        } else {
            val it = TodoItem(text = transferObj.lines, date = System.currentTimeMillis() ,number = transferObj.id)
            val itemsToAdd: MutableList<TodoItem> = ArrayList<TodoItem>()
            if (transferObj.lines.trim() != "") {
                itemsToAdd.add(it)
            }
            itemsToAdd
        }
    }


    override fun onCleared() {
        executor.shutdown()
    }


}