package com.example.userrequest.readAll

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.interfaces.userprofile_usecases.GetUserIdUseCase
import com.example.domain.interfaces.userprofile_usecases.GetUserRoleUseCase
import com.example.domain.interfaces.userrequest_usecases.GetAllUserRequestsUseCase
import com.example.userrequest.read.UserRequestListResponseModel
import com.example.userrequest.read.toContactListResponseModel
import com.hfad.model.Address
import com.hfad.model.BasicError
import com.hfad.model.Error
import com.hfad.model.Failure
import com.hfad.model.RepoResult
import com.hfad.model.Success
import com.hfad.model.Triumph
import com.hfad.model.UserRequestResponseModel
import com.hfad.model.ViewState
import com.hfad.model.onFailure
import com.hfad.model.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class ReadAllUserRequestsViewModel @Inject constructor(
    private val getAllUserRequestsUseCase: GetAllUserRequestsUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getUserRoleUseCase: GetUserRoleUseCase
) : ViewModel() {

    private val coroutineContext= CoroutineScope(Dispatchers.IO).coroutineContext
    private val _errorMessage = mutableStateOf("")


    val _requestViewState = MutableLiveData<ViewState<List<UserRequestListResponseModel>>>()
    val requestViewState: LiveData<ViewState<List<UserRequestListResponseModel>>>
        get() = _requestViewState


    init {
        viewModelScope.launch {

            getUserRole()}
    }

    val errorMessage : String
        get() = _errorMessage.value

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
        viewModelScope.launch(coroutineContext) {
            getAllUserRequestsUseCase.execute()
                .onSuccess{
                    val filteredRequests = it.filter { it.userId != getIdValue() }
                    Log.d("CONTACT",it.toString())
                    _requestViewState.postValue(Triumph(filteredRequests.map { it.toContactListResponseModel() }))
                }
                .onFailure{
                    _requestViewState.postValue(Error(BasicError(it.throwable) ,"getting all requests"))
                    _errorMessage.value = "Greška ${it.throwable.cause}"
                }

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

data class UserRequestListResponseModel(
    val uuid: String,
    val userId: String,
    val photo: Uri,
    val description: String,
    val address1: Address,
    val address2: Address,
    val timeTable: String,
    val category: String,
    val extraWorker: Boolean,
    val price: Int,
    val sid:String
)

fun UserRequestResponseModel.toContactListResponseModel()
        :UserRequestListResponseModel {
    return UserRequestListResponseModel(
        uuid = uuid!!,
        userId=userId,
        photo=photo,
        description=description,
        address1 = address1,
        address2 = address2,
        timeTable=timeTable,
        category=category,
        extraWorker=extraWorker,
        price=price,
        sid=sid ?: "",
        status="",
        offerPrice = 0

    )
}