package com.example.userrequest.readAll

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.interfaces.userprofile_usecases.GetUserIdUseCase
import com.example.domain.interfaces.userprofile_usecases.GetUserRoleUseCase
import com.example.domain.interfaces.userrequest_usecases.GetAllUserRequestsUseCase
import com.example.userrequest.read.UserRequestListResponseModel
import com.example.userrequest.read.toContactListResponseModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class ReadAllUserRequestsViewModel @Inject constructor(
    private val getAllUserRequestsUseCase: GetAllUserRequestsUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getUserRoleUseCase: GetUserRoleUseCase
) : ViewModel() {
    private val _errorMessage = mutableStateOf("")
    private val _userRequests = mutableStateListOf<UserRequestListResponseModel>()



    init {
        viewModelScope.launch {
            getAll()
        getUserRole()}
    }

    val errorMessage : String
        get() = _errorMessage.value

    val userRequests: List<UserRequestListResponseModel>
        get() = _userRequests.toList()

    suspend fun getUserId(): Result<String> {
        return getUserIdUseCase.execute()
    }

    fun getIdValue() : String{
        var r = ""
        runBlocking {
            r = getUserId().getOrNull()?:"none"
        }
        Log.d("VIEWMODEL-login",r)
        return r
    }
    suspend fun getAll() {
        try {
            _userRequests.clear()
            val list = getAllUserRequestsUseCase.execute(getIdValue())
            _userRequests.addAll(list.map {
                it.toContactListResponseModel()
            })
        }catch (e: Exception) {
            _errorMessage.value = "Greška ${e.message}"
        }
    }

    suspend fun getRole(): Result<String> {
        return getUserRoleUseCase.execute()
    }

    fun getUserRole() : String{
        var r = ""
        runBlocking {
            r = getRole().getOrNull()?:"none"
        }
        Log.d("VIEWMODEL-role",r)
        return r
    }
}