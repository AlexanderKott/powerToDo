package ru.mobiledevelopment.powertodo.ui.main

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Point
import android.os.Bundle
import android.view.*
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import ru.mobiledevelopment.powertodo.R
import ru.mobiledevelopment.powertodo.databinding.AddnewitemFragmentBinding
import ru.mobiledevelopment.powertodo.databinding.FragmentToDoBinding
import ru.mobiledevelopment.powertodo.ui.main.data.TodoItem
import java.util.*


class AddNewItemFragment : DialogFragment() {
    private var requestKey = "newFragmentBundle"
    private lateinit var binding: AddnewitemFragmentBinding


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AddnewitemFragmentBinding.inflate(requireActivity().layoutInflater)

        var updateItemId : Long = 0L
        getArguments()?.let { bundle ->
            bundle.getString("itemToEdit").let {
                val trObject: TodoItem? =
                    Utils.gsonParser?.fromJson(it, TodoItem::class.java)
                binding.addTodoItems.setText(trObject?.text)
                updateItemId = trObject?.number ?: 0
                binding.checkBoxManyLines.isGone = true
                requestKey = "editFragmentBundle"
            }
        }

        val builder = AlertDialog.Builder(activity)
        builder.setView(binding.root)
            .setPositiveButton("OK") { dialog, which ->
                setFragmentResult(
                    "EditItemDialogFragment",
                    bundleOf(
                        requestKey to
                                Utils.gsonParser?.toJson(
                                    ToDoInfoTransfer(
                                        updateItemId,
                                        binding.addTodoItems.text.toString(),
                                        binding.checkBoxManyLines.isChecked
                                    )
                                )
                    )
                )
            }
            .setNegativeButton("Cancel") { dialog, id -> getDialog()?.cancel() }

        val dialogWindow = builder.create()
        dialogWindow.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialogWindow
    }


    override fun onResume() {
        val window = dialog!!.window
        val size = Point()
        val display = window!!.windowManager.defaultDisplay
        display.getSize(size)
        window.setLayout((size.x * 0.9).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.CENTER)
        super.onResume()
    }


}