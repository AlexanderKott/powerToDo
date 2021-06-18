package site.kotty_kov.powertodo.todolist

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import kotlinx.coroutines.runBlocking
import site.kotty_kov.powertodo.R
import site.kotty_kov.powertodo.databinding.ActivityMainBinding
import site.kotty_kov.powertodo.todolist.main.common.*
import site.kotty_kov.powertodo.todolist.main.viewModel.ModelFactory
import site.kotty_kov.powertodo.todolist.main.viewModel.SharedViewModelCommon
import site.kotty_kov.powertodo.todolist.main.viewModel.SharedViewModelNotePad
import site.kotty_kov.powertodo.todolist.main.viewModel.SharedViewModelToDo


class MainActivity : AppCompatActivity() {

    private lateinit var viewModelToDo: SharedViewModelToDo
    private lateinit var vmCommon: SharedViewModelCommon

    private var firstTime: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vmCommon =
            ViewModelProviders.of(
                this,
                ModelFactory(App.getApp().getDatabase())
            ).get(SharedViewModelCommon::class.java)

        viewModelToDo =
            ViewModelProviders.of(
                this,
                ModelFactory(App.getApp().getDatabase())
            ).get(SharedViewModelToDo::class.java)

        val viewModelNotepad =
            ViewModelProviders.of(
                this,
                ModelFactory(App.getApp().getDatabase())
            ).get(SharedViewModelNotePad::class.java)


        //листенер ивентов от чилдов для открытия меню
        supportFragmentManager
            .setFragmentResultListener(Values.drawerMenuKey, this) { _, bundle ->
                bundle.getString(Values.btn_menuKey).let {
                    when (it) {
                        Values.todoKey -> {
                            requestTodoFragment(vmCommon)
                        }
                        Values.notepadKey -> {
                            requestNotepadFragment(vmCommon)
                        }
                        Values.settingsKey -> {
                            requestSettingsFragment(vmCommon)
                        }
                        Values.passwordPage -> {
                            runBlocking {
                                if (vmCommon.isPasswordProtected()) {
                                    requestPasswordFragment()
                                }
                            }


                        }

                    }
                }
            }

        //ивент на Unlock
        supportFragmentManager
            .setFragmentResultListener(Values.unlock, this) { _, bundle ->
                requestTodoFragment(vmCommon)
            }
    }


    private var appLockTime: Long = 0
    override fun onStart() {
        super.onStart()
        runBlocking {
            when (vmCommon.isPasswordProtected()) {
                true -> {
                    when {
                        appLockTime == 0L -> {
                            requestPasswordFragment()
                        }
                        appLockTime < System.currentTimeMillis() - Values.lockTime -> {
                            requestPasswordFragment()
                        }
                        else -> {
                            //do nothing
                        }
                    }
                }
                false -> {
                    if (firstTime) {
                        requestTodoFragment(vmCommon)
                        firstTime = false
                    }
                }
            }
        }
    }


    override fun onPause() {
        appLockTime = System.currentTimeMillis()
        viewModelToDo.saveColorButtonsState()
        super.onPause()
    }


    private var lastBackPressTime: Long = 0
    override fun onBackPressed() {

        if (supportFragmentManager.fragments.last().tag == Values.settingsFragment) {
            supportFragmentManager.popBackStack()
            return
        }
        when (vmCommon.getApplicationState()) {
            4 -> {
                requestNotepadFragment(vmCommon)
                return
            }

            5, 6 -> {
                requestTodoFragment(vmCommon, 1)
                return
            }
        }




        if (lastBackPressTime < System.currentTimeMillis() - Values.exitTime) {
            Toast.makeText(this, this.getString(R.string.confirmExit), Toast.LENGTH_SHORT)
                .show()
            lastBackPressTime = System.currentTimeMillis()
        } else {
            super.onBackPressed()
        }
    }

}