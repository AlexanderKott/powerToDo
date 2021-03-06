package ru.mobiledevelopment.powertodo.ui.main.viewToDo

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import ru.mobiledevelopment.powertodo.R
import ru.mobiledevelopment.powertodo.databinding.ItemLayoutCardBinding
import ru.mobiledevelopment.powertodo.ui.main.ToDoInfoTransfer
import ru.mobiledevelopment.powertodo.ui.main.data.TodoItem
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ItemsViewHolder(
    private val binding: ItemLayoutCardBinding,
    private val actionsListener: ActionsListener ) : RecyclerView.ViewHolder(binding.root) {


 fun bind (todoitem: TodoItem) {
     with(binding) {
         val text: TextView = todotext
         val date: TextView = tododate
         val number: TextView = number
         doneChb.isChecked = todoitem.done
         text.setText(todoitem.text)
         val currentDate: Date = Date(todoitem?.date ?: 0)
         val df: DateFormat = SimpleDateFormat("dd.MM.yy HH:mm:ss")
         date.setText(df.format(currentDate))
         number.setText(StringBuilder("#").append(todoitem?.number).toString())
         //holder.itemView.setBackgroundColor(todoitem.color);

         cardView.setOnLongClickListener(View.OnLongClickListener { view: View? ->
             actionsListener.cardViewLongClick(todoitem)
             true
         })


         doneChb.setOnClickListener(View.OnClickListener { view: View? ->
             todoitem.done = !todoitem.done
             actionsListener.doneCheckboxClick(todoitem)

         })

     }
 }


}
