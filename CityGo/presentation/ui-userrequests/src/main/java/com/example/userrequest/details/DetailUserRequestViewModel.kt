package com.example.userrequest.details

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.interfaces.offer_usecases.CreateOfferUseCase
import com.example.domain.interfaces.offer_usecases.HasOfferUseCase
import com.example.domain.interfaces.userprofile_usecases.GetUserIdUseCase
import com.example.domain.interfaces.userprofile_usecases.GetUserProfileUseCase
import com.example.domain.interfaces.userprofile_usecases.GetUserRoleUseCase
import com.example.domain.interfaces.userrequest_usecases.DeleteUserRequestUseCase
import com.example.domain.interfaces.userrequest_usecases.GetUserRequestUseCase
import com.example.domain.interfaces.userrequest_usecases.UpdateUserRequestUseCase
import com.hfad.model.Address
import com.hfad.model.OfferRequestModel
import com.hfad.model.UserRequestResponseModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class DetailUserRequestViewModel @Inject constructor(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getUserRequestUseCase: GetUserRequestUseCase,
    private val updateUserRequestUseCase: UpdateUserRequestUseCase,
    private val deleteUserRequestUseCase: DeleteUserRequestUseCase,
    private val createOfferUseCase: CreateOfferUseCase,
    private val getUserRoleUseCase: GetUserRoleUseCase,
    private val hasOfferUseCase: HasOfferUseCase
): ViewModel(){

    var state by mutableStateOf(UserRequestData())
    var data by mutableStateOf(ProfileState())
    var userId by mutableStateOf("")

    private val _price = mutableStateOf(state.price.toString())
    private val _timeTable = mutableStateOf(state.timeTable)


    val price: String
        get() = _price.value

    fun setPrice(value: String) {
        _price.value = value
    }

    val timeTable: String
        get() = _timeTable.value

    fun setTimeTable(value: String) {
        _timeTable.value = value
    }


    init {
        viewModelScope.launch {

        }

    }

    fun doesOfferExist(userRequestId: Int): Boolean {
        var count = 0
        runBlocking {
            count = hasOfferUseCase.execute(userRequestId, getIdValue())
        }
        Log.d("COUNTERR", count.toString())
        return count>0

    }

    suspend fun getUserRequest(requestId:Int,userId:String,){
        val request= getUserRequestUseCase.execute(userId,requestId)
        if (request!=null)
            state = state.copy(
                photo = request.photo,
                description = request.description,
                address1 = request.address1,
                address2 = request.address2,
                timeTable = request.timeTable,
                category = request.category,
                extraWorker = request.extraWorker,
                price = request.price

            )
    }

     fun getProfileDetails(userId: String){
        viewModelScope.launch{
            val user = getUserProfileUseCase.execute(userId)
            if (user != null) {
                Log.d("PROFIL",user.name)
            }
            if (user!=null)
                data = data.copy(
                    name=user.name,
                    stars = user.stars,
                    photo = user.profilePicture.toString()


                )
        }

    }

    fun deleteUserRequest(requestId: Int,userId: String)= viewModelScope.launch{
        deleteUserRequestUseCase.execute(userId,requestId)
    }


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

    fun createOffer(requestId:Long,price:Int,timeTable:String ){
        viewModelScope.launch { createOfferUseCase.execute(
            OfferRequestModel(
                id = null,
                userRequestId = requestId.toInt(),
                serviceProviderId =getIdValue(),
                price,
                timeTable,
                status = "Pending"
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