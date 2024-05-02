package com.example.ui_home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.domain.interfaces.userprofile_usecases.GetUserRoleUseCase
import com.example.domain.interfaces.userprofile_usecases.SetUserRoleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserRoleUseCase: GetUserRoleUseCase
): ViewModel()  {

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