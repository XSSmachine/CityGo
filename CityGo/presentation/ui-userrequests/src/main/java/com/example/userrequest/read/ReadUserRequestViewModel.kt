package com.example.userrequest.read

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.interfaces.offer_usecases.GetAllMyOffersUseCase
import com.example.domain.interfaces.offer_usecases.GetAllOffersUseCase
import com.example.domain.interfaces.offer_usecases.GetOfferUseCase
import com.example.domain.interfaces.offer_usecases.UpdateOfferStatusUseCase
import com.example.domain.interfaces.serviceproviderprofile_usecases.GetServiceProviderProfileUseCase
import com.example.domain.interfaces.userprofile_usecases.GetUserIdUseCase
import com.example.domain.interfaces.userprofile_usecases.GetUserRoleUseCase
import com.example.domain.interfaces.userrequest_usecases.GetAllCurrentUserRequestsUseCase
import com.example.domain.interfaces.userrequest_usecases.GetAllUserRequestsUseCase
import com.example.domain.interfaces.userrequest_usecases.GetUserRequestByIdUseCase
import com.hfad.model.Address
import com.hfad.model.OfferResponseModel
import com.hfad.model.UserRequestResponseModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

data class UserRequestListResponseModel(
    val id: Int,
    val userId: String,
    val photo: Uri,
    val description: String,
    val address1: Address,
    val address2: Address,
    val timeTable: String,
    val category: String,
    val extraWorker: Boolean,
    val price: Int
)

fun UserRequestResponseModel.toContactListResponseModel()
        :UserRequestListResponseModel {
    return UserRequestListResponseModel(
        id = id,
        userId=userId,
        photo=photo,
        description=description,
        address1 = address1,
        address2 = address2,
        timeTable=timeTable,
        category=category,
        extraWorker=extraWorker,
        price=price,

        )
}



data class OfferListResponseModel(
    val id: Int,
    val userRequestId: Int,
    val serviceProviderId:String,
    val timeTable: String?,
    val status: String,
    val price: Int?,
    val serviceProviderImage: String,
    val serviceProviderName:String,
    val serviceProviderStars: Double,


)

fun OfferResponseModel.toOfferListResponseModel(serviceProviderProfileState: ServiceProviderProfileState): OfferListResponseModel {
    return OfferListResponseModel(
        id = id,
        userRequestId =userRequestId,
        serviceProviderId = serviceProviderId,
        price =price,
        timeTable = timeTable,
        status = status,
        serviceProviderName = serviceProviderProfileState.name,
        serviceProviderImage = serviceProviderProfileState.profilePicture,
        serviceProviderStars = serviceProviderProfileState.stars


        )
}
@HiltViewModel
class ReadUserRequestViewModel @Inject constructor(
    private val getAllContactsUseCase: GetAllCurrentUserRequestsUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getAllOffersUseCase: GetAllOffersUseCase,
    private val getServiceProviderStatusUseCase: GetServiceProviderProfileUseCase,
    private val getUserRequestByIdUseCase: GetUserRequestByIdUseCase,
    private val getAllMyOffersUseCase: GetAllMyOffersUseCase,
    private val getUserRoleUseCase: GetUserRoleUseCase,
    private val getOfferUseCase: GetOfferUseCase,
    private val updateOfferStatusUseCase: UpdateOfferStatusUseCase
) : ViewModel() {



    private val _errorMessage = mutableStateOf("")
    private val _userRequests = mutableStateListOf<UserRequestListResponseModel>()
    private val _offers = mutableStateListOf<OfferListResponseModel>()
    private val _sp_offers = mutableStateListOf<OfferResponseModel>()
    private val _sp_userRequests = mutableStateListOf<UserRequestListResponseModel>()
    var providerState by mutableStateOf(ServiceProviderProfileState())

    val errorMessage : String
        get() = _errorMessage.value

    val userRequests: List<UserRequestListResponseModel>
        get() = _userRequests.toList()

    val SPuserRequests: List<UserRequestListResponseModel>
        get() = _sp_userRequests.toList()

    val offers: List<OfferListResponseModel>
        get() = _offers.toList()


    fun checkOffersForRequest(userRequestId: Int): Boolean {
        var check=false
        runBlocking{
            check = getAllOffersUseCase.execute(userRequestId).isNotEmpty() }
        return check
    }

    fun getSingleOfferStatus(requestId: Int):String{
         var status = ""
         runBlocking {
             val offer = getOfferUseCase.execute(requestId,getIdValue())
             if (offer != null){
                 status = offer.status
             }
              }

         return status
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

    fun updateOfferStatus(requestId: Int,serviceProviderId: String,status: String){
        runBlocking { updateOfferStatusUseCase.execute(requestId,serviceProviderId,status) }
    }


    fun getUserRequestById(requestId: Int): UserRequestResponseModel? {
        var userRequest = UserRequestResponseModel(
            id = 123,
            userId = "user123",
            photo = Uri.parse("content://com.example.app/photo/123"),
            description = "Fix leaky faucet",
            address1 = Address("123 Main St", null, null, false,0,"",""),
            address2 = Address("456 Elm St", null, null, false,0,"",""),
            timeTable = "9:00 AM - 5:00 PM",
            date = "2024-05-01",
            category = "Plumbing",
            extraWorker = false,
            price = 100
        )
        runBlocking { userRequest = getUserRequestByIdUseCase.execute(requestId)!! }
        return userRequest
    }

    suspend fun getServiceProviderOffers() {
        _sp_offers.clear()
        _sp_userRequests.clear()
        val list = getAllMyOffersUseCase.execute(getIdValue())
        _sp_offers.addAll(list)

        for (offer in list) {
            val userRequest = getUserRequestById(offer.userRequestId)
            userRequest?.let { _sp_userRequests.add(it.toContactListResponseModel()) }
        }
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
    suspend fun getMyUserRequest() {
        try {
            _userRequests.clear()
            val list = getAllContactsUseCase.execute(getIdValue())
            _userRequests.addAll(list.map {
                it.toContactListResponseModel()
            })
        }catch (e: Exception) {
            _errorMessage.value = "Greška ${e.message}"
        }
    }

    suspend fun getOffers(requestId:Long) {
        try {
            _userRequests.clear()
            val list = getAllOffersUseCase.execute(requestId.toInt())
            _offers.addAll(list.map {
                it.toOfferListResponseModel(getServiceProviderProfileDetails(it.serviceProviderId))
            })
        }catch (e: Exception) {
            _errorMessage.value = "Greška ${e.message}"
        }
    }

    @SuppressLint("SuspiciousIndentation")
    suspend fun getServiceProviderProfileDetails(UserId:String):ServiceProviderProfileState{
        val user = getServiceProviderStatusUseCase.execute(UserId)
        if (user!=null)
            providerState = providerState.copy(
                id = user.id,
                name=user.name,
                surname=user.surname,
                email=user.email,
                stars = user.stars,
                profilePicture=user.vehiclePicture,
                )
        return providerState
    }


}

data class ServiceProviderProfileState(
    val id:String="",
    val name:String="",
    val surname:String="",
    val email:String="",
    val profilePicture:String="",
    val stars:Double=0.0,

    )