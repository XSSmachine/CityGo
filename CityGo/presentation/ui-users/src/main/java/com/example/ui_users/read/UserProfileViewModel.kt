package com.example.ui_users.read

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.domain.interfaces.userprofile_usecases.CreateUserProfileUseCase
import com.example.domain.interfaces.userprofile_usecases.GetUserProfileUseCase
import com.example.domain.interfaces.userprofile_usecases.UpdateUserProfileUseCase
import com.google.firebase.auth.FirebaseAuth
import com.hfad.model.UserProfileRequestModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase
    ): ViewModel(){
    private val _profilePicture = mutableStateOf("")
    private val _name = mutableStateOf("")
    private val _surname = mutableStateOf("")
    private val _email = mutableStateOf("")

    val profilePicture: String
        get() = _profilePicture.value

    fun setProfilePicture(value: String) {
        _profilePicture.value = value
    }

    val name: String
        get() = _name.value

    fun setName(value: String) {
        _name.value = value
    }

    val surname: String
        get() = _surname.value

    fun setSurname(value: String) {
        _surname.value = value
    }

    val email: String
        get() = _email.value

    fun setEmail(value: String) {
        _email.value = value
    }

    suspend fun getProfileDetails(phoneNum:String){
        getUserProfileUseCase.execute(phoneNum)
    }



}