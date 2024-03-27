package com.example.ui_users.login

import android.app.Activity
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.domain.interfaces.userprofile_usecases.CheckIfUserProfileExistUseCase
import com.example.domain.interfaces.userprofile_usecases.CreateUserProfileUseCase
import com.example.domain.interfaces.userprofile_usecases.GetUserIdUseCase
import com.example.domain.interfaces.userprofile_usecases.ReadUserIdUseCase
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.hfad.model.UserProfileRequestModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class UserLoginViewModel @Inject constructor(
    private val createUserProfileUseCase: CreateUserProfileUseCase,
    private val checkIfUserProfileExistUseCase: CheckIfUserProfileExistUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val readUserIdUseCase: ReadUserIdUseCase
):ViewModel(){

    val user = Firebase.auth.currentUser

    private val _errorMessage = mutableStateOf("")
    private val _phoneNumber = mutableStateOf("")
    private val _name = mutableStateOf("")
    private val _surname = mutableStateOf("")
    private val _email = mutableStateOf("")

    val phoneNumber: String
        get() = _phoneNumber.value

    fun setPhoneNumber(value: String) {
        _phoneNumber.value = value
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

    suspend fun createUser() {
        try {
            createUserProfileUseCase.execute(
                UserProfileRequestModel(
                id=null,
                name= _name.value,
                surname= _surname.value,
                phoneNumber=_phoneNumber.value,
                email = _email.value,
                profilePicture = null,
                stars = 4.00,

            ))
        } catch (e: Exception) {
            _errorMessage.value = "Error ${e.message}"
        }
    }

    suspend fun getUserId(userId:String){
        getUserIdUseCase.execute(userId)
    }

    suspend fun readUserId(): Flow<String?> {
        return readUserIdUseCase.execute()
    }

    suspend fun checkIfUserExists(phoneNumber: String) : Boolean{
        try {
            return checkIfUserProfileExistUseCase.execute(phoneNumber)

        }catch (e: Exception) {
            _errorMessage.value = "Error ${e.message}"
        }
        return TODO("Provide the return value")
    }
    fun sendVerificationCode(
        number: String,
        auth: FirebaseAuth,
        activity: Activity,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }


}