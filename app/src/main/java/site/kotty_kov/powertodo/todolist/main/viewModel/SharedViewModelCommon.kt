package site.kotty_kov.powertodo.todolist.main.viewModel

import androidx.lifecycle.*
import site.kotty_kov.powertodo.todolist.main.common.App
import site.kotty_kov.powertodo.todolist.main.common.Values
import site.kotty_kov.powertodo.todolist.main.data.db.TodoRoomDatabase
import site.kotty_kov.powertodo.todolist.main.data.user.UserInfo


class SharedViewModelCommon(base: TodoRoomDatabase, application: App) :
    AndroidViewModel(application) {


    //----------- data sources ---------------

    //Shared prefs
    private val colorButtons: SharedPrefsRepository = SharedPrefsRepositoryImpl(application.applicationContext)

    //Room
    private val roomRepo : RoomRepository = RoomRepositoryImpl(base)


    //-------------------------- variables -----------------------------------------
    //Common

    //main activity
    suspend fun isPasswordProtected(): Boolean {
        val userInfo = roomRepo.getUserInfo()
        return if (userInfo != null){
            userInfo.password != Values.defaultPassword
        } else {
            roomRepo.insertUserInfo(UserInfo(id = 0, "", Values.defaultPassword, 0))
            false
        }
    }


    //Login fragment
    suspend fun getPassword(): String {
        return roomRepo.getUserInfo().password
    }


    fun getUserInfo(): LiveData<UserInfo> {
        return roomRepo.getUserInf()
    }


    fun updateItem(it: UserInfo) {
        roomRepo.updateUserInfo(it)
    }


///--------------------Application states -------------------

    private var applicationDisplayState: Int = 0

    fun getApplicationState(): Int {
        return applicationDisplayState
    }

    fun setAppStateToDo() {
        applicationDisplayState = 0
    }

    fun setStateNotepad() {
        applicationDisplayState = 1
    }

    fun setPasswordLockScr() {
        applicationDisplayState = 2
    }

    fun setSettings() {
        applicationDisplayState = 3
    }

    fun setEditNotepadRecord() {
        applicationDisplayState = 4
    }

    fun setLastInPorgressScreenStateAsTitmer() {
        applicationDisplayState =  5 //1
    }

    fun setLastInPorgressScreenStateAsRecordEdit() {
        applicationDisplayState =  6  //2
    }

    fun setLastInPorgressScreenStateAsList() {
        applicationDisplayState =  7 //0
    }



    //---------------------------------
    override fun onCleared() {
        roomRepo.shutdown()
    }


}