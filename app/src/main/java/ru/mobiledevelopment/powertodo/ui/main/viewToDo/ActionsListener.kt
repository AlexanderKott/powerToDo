package ru.mobiledevelopment.powertodo.ui.main.viewToDo

import ru.mobiledevelopment.powertodo.ui.main.data.TodoItem

interface ActionsListener {
      fun cardViewLongClick(todoitem: TodoItem)
      fun doneCheckboxClick(todoitem: TodoItem)

}
