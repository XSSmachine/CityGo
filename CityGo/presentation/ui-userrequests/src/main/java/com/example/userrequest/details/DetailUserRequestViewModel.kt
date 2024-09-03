package com.example.userrequest.details

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.interfaces.offer_usecases.CheckCreatedOffersUseCase
import com.example.domain.interfaces.offer_usecases.CreateOfferUseCase
import com.example.domain.interfaces.userprofile_usecases.GetUserIdUseCase
import com.example.domain.interfaces.userprofile_usecases.GetUserProfileUseCase
import com.example.domain.interfaces.userprofile_usecases.GetUserRoleUseCase
import com.example.domain.interfaces.userrequest_usecases.DeleteUserRequestUseCase
import com.example.domain.interfaces.userrequest_usecases.GetRemoteUserRequestUseCase
import com.example.domain.interfaces.userrequest_usecases.GetUserRequestUseCase
import com.example.userrequest.R
import com.hfad.model.Address
import com.hfad.model.Error
import com.hfad.model.Loading
import com.hfad.model.OfferRequestModel
import com.hfad.model.Success
import com.hfad.model.Triumph
import com.hfad.model.UserProfileResponseModel
import com.hfad.model.UserRequestResponseModel
import com.hfad.model.ViewState
import com.hfad.model.onFailure
import com.hfad.model.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailUserRequestViewModel @Inject constructor(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getUserRequestUseCase: GetUserRequestUseCase,
    private val deleteUserRequestUseCase: DeleteUserRequestUseCase,
    private val createOfferUseCase: CreateOfferUseCase,
    private val getUserRoleUseCase: GetUserRoleUseCase,
    private val getRemoteUserRequestUseCase: GetRemoteUserRequestUseCase,
    private val checkCreatedOffersUseCase: CheckCreatedOffersUseCase
): ViewModel(){

    private val coroutineContext= CoroutineScope(Dispatchers.IO).coroutineContext

    protected val _requestViewState = MutableLiveData<ViewState<UserRequestResponseModel>>()
    val requestViewState: LiveData<ViewState<UserRequestResponseModel>>
        get() = _requestViewState


    protected val _profileViewState = MutableLiveData<ViewState<UserProfileResponseModel>>()
    val profileViewState: LiveData<ViewState<UserProfileResponseModel>>
        get() = _profileViewState

    var data by mutableStateOf(ProfileState())
    var userId by mutableStateOf("")

    init {
        viewModelScope.launch {
            _requestViewState.postValue(Loading("Loading", "Loading", "Loading", R.drawable.baseline_rocket_launch_24, R.drawable.baseline_rocket_launch_24))
            _profileViewState.postValue(Loading("Loading", "Loading", "Loading", R.drawable.baseline_rocket_launch_24, R.drawable.baseline_rocket_launch_24))
        }

    }

    suspend fun doesOfferExist(sid: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = checkCreatedOffersUseCase.execute(sid, getIdValue())
                when (response) {
                    is Success -> true
                    else -> false
                }
            } catch (e: Exception) {
                Log.d("doesOfferExist", "Error checking offer existence: $e")
                false
            }
        }
    }

    suspend fun getUserRequest(userRequestUUID: String,userId:String,){
        viewModelScope.launch(coroutineContext) {
            getUserRequestUseCase.execute(userId, userRequestUUID)
                .onSuccess { _requestViewState.postValue(Triumph(it)) }
                .onFailure { _requestViewState.postValue(Error(it, "user request details")) }
        }

    }

    suspend fun getRemoteUserRequest(sid:String){
        viewModelScope.launch(coroutineContext) {
            getRemoteUserRequestUseCase.execute(sid)
                .onSuccess { _requestViewState.postValue(Triumph(it)) }
                .onFailure { _requestViewState.postValue(Error(it, "user request details")) }
        }
    }

     fun getProfileDetails(userId: String){
        viewModelScope.launch(coroutineContext){
            getUserProfileUseCase.execute(userId)
                .onSuccess { _profileViewState.postValue(Triumph(it)) }
                .onFailure { _profileViewState.postValue(Error(it)) }
        }

    }

    fun deleteUserRequest(userRequestUUID: String,userId: String)= viewModelScope.launch{
        deleteUserRequestUseCase.execute(userId,userRequestUUID)
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

    suspend fun getRole(): Result<String> {
        return getUserRoleUseCase.execute()
    }

    fun getUserRole() : String{
        var r = ""
        runBlocking {
            r = getRole().getOrNull()?:"none"
        }
        return r
    }

    fun createOffer(userRequestUUID: String,price:Int,timeTable:String ){
        viewModelScope.launch { createOfferUseCase.execute(
            OfferRequestModel(
                id = null,
                userRequestUUID = userRequestUUID,
                serviceProviderId =getIdValue(),
                price,
                timeTable,
                status = "Pending",
                sid = null,
                sync = null
            )
        ) }

    }

}

data class UserRequestData(
    val photo: Uri="".toUri(),
    val description: String="",
    val address1: Address= Address("",null,null,true,0,"",""),
    val address2: Address=Address("",null,null,true,0,"",""),
    val timeTable: String="",
    val category: String="",
    val extraWorker: Boolean=false,
    val price: Int=0
)

data class ProfileState(
    val name:String="",
    val stars:Double=0.0,
    val photo: String=""

    )