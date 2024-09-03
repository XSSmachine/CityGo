package com.example.ui_users.login

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.domain.interfaces.DataStoreRepository
import com.example.domain.interfaces.userprofile_usecases.CheckIfUserProfileExistUseCase
import com.example.domain.interfaces.userprofile_usecases.CreateUserProfileUseCase
import com.example.domain.interfaces.userprofile_usecases.GetUserIdUseCase
import com.example.domain.interfaces.userprofile_usecases.SetUserIdUseCase
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import com.hfad.model.BasicError
import com.hfad.model.Failure
import com.hfad.model.Success
import com.hfad.model.UserProfileRequestModel
import com.hfad.model.onFailure
import com.hfad.model.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltViewModel
class UserLoginViewModel @Inject constructor(
    private val createUserProfileUseCase: CreateUserProfileUseCase,
    private val checkIfUserProfileExistUseCase: CheckIfUserProfileExistUseCase,
    private val setUserIdUseCase: SetUserIdUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
):ViewModel(){
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

    fun clearAll(){
        setPhoneNumber("")
        setName("")
        setSurname("")
        setEmail("")
    }

    suspend fun createUser() {
        try {
            createUserProfileUseCase.execute(
                UserProfileRequestModel(
                id =getIdValue(),
                name = _name.value,
                surname = _surname.value,
                phoneNumber ="0"+_phoneNumber.value,
                email = _email.value,
                profilePicture = "",
                stars = 4.00,
                    sid = null,
                    sync = null,
                    requests = null

            ))
        } catch (e: Exception) {
            _errorMessage.value = "Error ${e.message}"
        }
    }

    suspend fun setUserId(userId:String){
        setUserIdUseCase.execute(userId)
    }

    suspend fun getUserId(): Result<String> {
        return getUserIdUseCase.execute()
    }



    fun getIdValue() : String{
        var r = ""
        runBlocking {
            r = getUserId().getOrNull()?:"none"
        }
        return r
    }



    suspend fun checkIfUserExists(phoneNumber: String) : Boolean{
        try {
            checkIfUserProfileExistUseCase.execute("\"0"+phoneNumber+"\"")
                .onSuccess { return it }
                .onFailure { Failure(BasicError(Throwable("Error"))) }

        }catch (e: Exception) {
            _errorMessage.value = "Error ${e.message}"
        }
        return false
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

