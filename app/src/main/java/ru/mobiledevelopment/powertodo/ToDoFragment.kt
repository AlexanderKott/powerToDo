package ru.mobiledevelopment.powertodo

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.mobiledevelopment.powertodo.databinding.FragmentToDoBinding
import ru.mobiledevelopment.powertodo.ui.main.*
import ru.mobiledevelopment.powertodo.ui.main.data.TodoItem
import ru.mobiledevelopment.powertodo.ui.main.viewModel.ModelFactory
import ru.mobiledevelopment.powertodo.ui.main.viewModel.ToDoViewModel
import ru.mobiledevelopment.powertodo.ui.main.viewToDo.ActionsListener
import ru.mobiledevelopment.powertodo.ui.main.viewToDo.ItemsAdapter


class ToDoFragment : Fragment() {

   private lateinit var recycler : RecyclerView
   private lateinit var rAdapter : ItemsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentToDoBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentToDoBinding.bind(view)

        val viewModel =
            ViewModelProviders.of(
                this,
                ModelFactory(App.getApp().getDatabase())
            ).get(ToDoViewModel::class.java)

        val adapter = ItemsAdapter(object : ActionsListener {
            override fun cardViewLongClick(todoitem: TodoItem) {
                activity?.supportFragmentManager?.let {
                    AddNewItemFragment().apply {
                        arguments = bundleOf("itemToEdit" to Utils.gsonParser?.toJson(todoitem))
                        show(it, "")
                    }
                }
            }

            override fun doneCheckboxClick(todoitem: TodoItem) {
                viewModel.updateItem(todoitem)
            }


        })
        recycler   = binding.todoListRecycler
        rAdapter = adapter
        binding.todoListRecycler.layoutManager = LinearLayoutManager(activity)
        binding.todoListRecycler.adapter = adapter

         val sih = object : SwipeItemHelper(requireContext(),
             object : DeleteListener {
                 override fun onItemDelete(pos: Int) {
                     viewModel.deleteItem(adapter.getItemById(pos))
                 }
             }){}

        sih.leftBG = ContextCompat.getColor(requireContext(), R.color.purple_500)
        sih.leftLabel = "Thumbs UP"
        sih.leftIcon = AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.ic_check_circle_black_24dp
        )

             //configure right swipe
        sih.rightBG = ContextCompat.getColor(requireContext(), R.color.red)
        sih.rightLabel = "Thumbs Down"
        sih.rightIcon = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_checked)

        val touchHelper = ItemTouchHelper(sih)
        touchHelper.attachToRecyclerView(binding.todoListRecycler)


        viewModel.items.observe(viewLifecycleOwner, { posts ->
            adapter.submitList(posts)
            viewModel.setItemsCount(posts.size)
        })

        viewModel.count.observe(viewLifecycleOwner, { value ->
            binding.switcher.displayedChild = value
        })

        binding.AddNew.setOnClickListener {
            activity?.supportFragmentManager?.let { AddNewItemFragment().show(it, "") }
        }


        setFragmentResultListener("EditItemDialogFragment") { requestKey, bundle ->
            bundle.getString("newFragmentBundle")?.let {
                val trObject: ToDoInfoTransfer? =
                    Utils.gsonParser?.fromJson(it, ToDoInfoTransfer::class.java)
                Log.e("abc", "abc1 =" + trObject.toString())

                if (trObject != null) {
                    viewModel.addNewItems(trObject)

                    recycler.postDelayed(Runnable {
                        recycler.smoothScrollToPosition(0)
                    }, 1_000)


                }
            }

            bundle.getString("editFragmentBundle")?.let { param ->
                Utils.gsonParser?.fromJson(param, ToDoInfoTransfer::class.java)?.let {
                    Log.e("abc", "abc2 =" + it.toString())
                    viewModel.updateItem(it)
                }

            }

        }

    }


}