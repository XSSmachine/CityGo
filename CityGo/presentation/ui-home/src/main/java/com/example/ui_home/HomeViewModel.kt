package com.example.ui_home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.interfaces.userprofile_usecases.GetUserRoleUseCase
import com.example.domain.interfaces.userprofile_usecases.SetUserRoleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserRoleUseCase: GetUserRoleUseCase
) : ViewModel() {

    private val _userRole = MutableStateFlow<String>("Owner")
    val userRole: StateFlow<String> = _userRole.asStateFlow()

    init {
        viewModelScope.launch {
            getUserRole()
        }
    }

    suspend fun getUserRole() {
        val result = getUserRoleUseCase.execute()
        _userRole.emit(result.getOrNull() ?: "Owner")
    }

    fun setUserRole(role: String) {
        viewModelScope.launch {
            _userRole.emit(role)
        }
    }
}
