package com.example.userrequest.read

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.interfaces.offer_usecases.DeleteOfferUseCase
import com.example.domain.interfaces.offer_usecases.GetAllMyOffersUseCase
import com.example.domain.interfaces.offer_usecases.GetAllOffersUseCase
import com.example.domain.interfaces.offer_usecases.GetOfferByUserRequestUseCase
import com.example.domain.interfaces.offer_usecases.GetOfferUseCase
import com.example.domain.interfaces.offer_usecases.UpdateOfferStatusUseCase
import com.example.domain.interfaces.serviceproviderprofile_usecases.GetServiceProviderProfileUseCase
import com.example.domain.interfaces.userprofile_usecases.GetUserIdUseCase
import com.example.domain.interfaces.userprofile_usecases.GetUserRoleUseCase
import com.example.domain.interfaces.userrequest_usecases.GetAllCurrentUserRequestsUseCase
import com.example.domain.interfaces.userrequest_usecases.GetAllUserRequestsUseCase
import com.example.domain.interfaces.userrequest_usecases.GetRemoteUserRequestUseCase
import com.example.domain.interfaces.userrequest_usecases.GetUserRequestByIdUseCase
import com.example.userrequest.R
import com.hfad.model.Address
import com.hfad.model.BasicError
import com.hfad.model.Error
import com.hfad.model.Failure
import com.hfad.model.Loading
import com.hfad.model.OfferResponseModel
import com.hfad.model.ServiceProviderProfileResponseModel
import com.hfad.model.Success
import com.hfad.model.Triumph
import com.hfad.model.UserRequestResponseModel
import com.hfad.model.ViewState
import com.hfad.model.onFailure
import com.hfad.model.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

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
        sid=sid ?: ""

        )
}



data class OfferListResponseModel(
    val id: Int,
    val userRequestUUID: String,
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
        userRequestUUID =userRequestUUID,
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
    private val getAllCurrentUserRequestsUseCase: GetAllCurrentUserRequestsUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getAllOffersUseCase: GetAllOffersUseCase,
    private val getServiceProviderStatusUseCase: GetServiceProviderProfileUseCase,
    private val getUserRequestByIdUseCase: GetUserRequestByIdUseCase,
    private val getAllMyOffersUseCase: GetAllMyOffersUseCase,
    private val getUserRoleUseCase: GetUserRoleUseCase,
    private val getOfferUseCase: GetOfferUseCase,
    private val getOfferByUserRequestUseCase: GetOfferByUserRequestUseCase,
    private val updateOfferStatusUseCase: UpdateOfferStatusUseCase,
    private val getRemoteUserRequestUseCase: GetRemoteUserRequestUseCase,
    private val deleteOfferUseCase: DeleteOfferUseCase
) : ViewModel() {


    private val _errorMessage = mutableStateOf("")
    //-------------------------------------------------------------------------------


    protected val _userRequests = MutableLiveData<ViewState<List<UserRequestListResponseModel>>>()
    val userRequests: LiveData<ViewState<List<UserRequestListResponseModel>>>
        get() = _userRequests

    protected val _offers = MutableLiveData<ViewState<List<OfferListResponseModel>>>()
    val offers: LiveData<ViewState<List<OfferListResponseModel>>>
        get() = _offers

    protected val _singleOffer = MutableLiveData<ViewState<OfferResponseModel>>()
    val singleOffer: LiveData<ViewState<OfferResponseModel>>
        get() = _singleOffer

    //-------------------------------------------------------------------------------

    protected val _sp_userRequests =
        MutableLiveData<ViewState<List<UserRequestListResponseModel>>>()
    val spUserRequests: LiveData<ViewState<List<UserRequestListResponseModel>>>
        get() = _sp_userRequests

    protected val _sp_offers = MutableLiveData<ViewState<List<OfferResponseModel>>>()
    val spOffers: LiveData<ViewState<List<OfferResponseModel>>>
        get() = _sp_offers


    //-------------------------------------------------------------------------------

    protected val _providerState = MutableLiveData<ViewState<ServiceProviderProfileState>>()
    val providerState: LiveData<ViewState<ServiceProviderProfileState>>
        get() = _providerState

    val errorMessage: String
        get() = _errorMessage.value

    init {
//        _offers.postValue(Loading("Loading", "Loading", "Loading", R.drawable.baseline_rocket_launch_24, R.drawable.img))
    }


    suspend fun checkOffersForRequest(userRequestUUID: String): Boolean {
        return withContext(viewModelScope.coroutineContext) {
            var check = false
            Log.d("userOffer-checkOffersInput", userRequestUUID)
            getAllOffersUseCase.execute(userRequestUUID)
                .onSuccess {
                    Log.d("userOffer-checkOffers1", it.toString())
                    if (it.size > 0) check = true
                }
                .onFailure {
                    Log.d("userOffer-checkOffers2", it.toString())
                    Failure(it)
                }
            check
        }
    }

    suspend fun getSingleOffer(sid: String):OfferResponseModel?{
        viewModelScope.launch(coroutineContext) {
            Log.d("userOffer-getByUserRequest",sid)
            getOfferByUserRequestUseCase.execute("\""+sid+" \"")
                .onSuccess {
                    Log.d("userOffer-Sucess",it.toString())
                    _singleOffer.postValue(Triumph(it)) }
                .onFailure {
                    Log.d("userOffer-Sucess",it.throwable.toString())
                    _singleOffer.postValue(Error(BasicError(it.throwable), "getting all sp offers")) }
        }
        return null
    }

    suspend fun getSingleOfferStatus(sid: String): String {
        var status = ""
        viewModelScope.launch(coroutineContext) {
            getOfferUseCase.execute(sid.trim())
                .onSuccess { status = it.status }
                .onFailure { Failure(it) }
        }

        return status
    }

    suspend fun deleteOffer(sid: String){
        viewModelScope.launch(coroutineContext) {
            Log.d("DELETE_OFFER",sid)
            deleteOfferUseCase.execute(sid)
        }
    }

    suspend fun getRole(): Result<String> {
        return getUserRoleUseCase.execute()
    }

    fun getUserRole(): String {
        var r = ""
        runBlocking {
            r = getRole().getOrNull() ?: "none"
        }
        Log.d("VIEWMODEL-role", r)
        return r
    }

    fun updateOfferStatus(sid: String, status: String) {
        runBlocking { updateOfferStatusUseCase.execute(sid, status) }
    }


    suspend fun getUserRequestById(userRequestUUID: String): UserRequestResponseModel {
        var userRequest = UserRequestResponseModel(
            uuid = "123",
            userId = "user123",
            photo = Uri.parse("content://com.example.app/photo/123"),
            description = "Fix leaky faucet",
            address1 = Address("123 Main St", null, null, false, 0, "", ""),
            address2 = Address("456 Elm St", null, null, false, 0, "", ""),
            timeTable = "9:00 AM - 5:00 PM",
            date = "2024-05-01",
            category = "Plumbing",
            extraWorker = false,
            price = 100,
            sid = "",
            sync = null,
            offers = null
        )
        viewModelScope.launch(coroutineContext) {
            getUserRequestByIdUseCase.execute(userRequestUUID)
                .onSuccess { userRequest = it }
        }
        return userRequest
    }

    suspend fun getServiceProviderOffers() {
        viewModelScope.launch(coroutineContext) {
            getAllMyOffersUseCase.execute(getIdValue())
                .onSuccess { offers ->
                    Log.d("userOffer2",offers.toString())
                    _sp_offers.postValue(Triumph(offers))
                    val userRequests = mutableListOf<UserRequestListResponseModel>()
                    offers.forEach { offer ->
                        getRemoteUserRequestUseCase.execute(offer.userRequestUUID.trim())
                            .onSuccess { userRequest ->
                                userRequest?.let {
                                    Log.d("userOffer",it.toContactListResponseModel().toString())
                                    userRequests.add(
                                        it.toContactListResponseModel())
                                }
                            }
                            .onFailure { exception ->
                                Log.d("userOffer",exception.toString())
                                _sp_userRequests.postValue(
                                    Error(
                                        exception,
                                        "getting all sp offers"
                                    )
                                )
                            }
                    }
                    _sp_userRequests.postValue(Triumph(userRequests))
                }
                .onFailure { exception ->
                    Log.d("userOffer1",exception.toString())
                    _sp_userRequests.postValue(Error(exception, "getting all sp offers"))
                }
        }
    }


    suspend fun getUserId(): Result<String> {
        return getUserIdUseCase.execute()
    }

    fun getIdValue(): String {
        var r = ""
        runBlocking {
            r = getUserId().getOrNull() ?: "none"
        }
        Log.d("VIEWMODEL-login", r)
        return r
    }

    suspend fun getMyUserRequest() {
        viewModelScope.launch(coroutineContext) {
            getAllCurrentUserRequestsUseCase.execute(getIdValue())
                .onSuccess { _userRequests.postValue(Triumph(it.map { it.toContactListResponseModel() })) }
                .onFailure { _userRequests.postValue(Error(it, "getting all my user requests")) }
        }
    }

    suspend fun getOffers(sid: String) {
        val offerListResponseModels = mutableListOf<OfferListResponseModel>()
        viewModelScope.launch(coroutineContext)  {
            getAllOffersUseCase.execute(sid)
                .onSuccess { offers ->
                    Log.d("WIN", offers.toString())

                    offers.forEach { offer ->

                        when (val result = getServiceProviderStatusUseCase.execute(offer.serviceProviderId)) {
                            is Success -> {
                                Log.d("WIN1", result.toString())
                                val offerListResponseModel = offer.toOfferListResponseModel(result.data.toServiceProviderProfileState())
                                offerListResponseModels.add(offerListResponseModel)
                                Log.d("WIN3", offerListResponseModels.toString())

                            }
                            is Failure -> {
                                _offers.postValue(Error(result.error,"getting all my offers"))
                            }
                            else -> {}
                        }
                    }
                    Log.d("WIN3.5", offerListResponseModels.toString())
                    _offers.postValue(Triumph(offerListResponseModels.toList()))
                    Log.d("WIN4", _offers.value.toString())
                }
                .onFailure { exception ->
                    _offers.postValue(Error(exception,"getting all my offers"))
                }
        }
    }


    @SuppressLint("SuspiciousIndentation")
    suspend fun getServiceProviderProfileDetails(UserId: String): ViewState<ServiceProviderProfileState>? {
        viewModelScope.launch(coroutineContext) {
            getServiceProviderStatusUseCase.execute(UserId)
                .onSuccess {
                    Log.d("WIN_LOSS", it.toString())
                    _providerState.postValue(Triumph(it.toServiceProviderProfileState())) }
                .onFailure { exception ->
                    Log.d("WIN_LOSS", exception.toString())
                    _providerState.postValue(Error(exception, "getting sp details"))
                }
        }
        return _providerState.value


    }
}


fun ServiceProviderProfileResponseModel.toServiceProviderProfileState()
        : ServiceProviderProfileState {
    return ServiceProviderProfileState(
        id = id,
        name=name,
        surname=surname,
        email=email,
        stars = stars,
        profilePicture=vehiclePicture,

        )
}

data class ServiceProviderProfileState(
    val id:String,
    val name:String,
    val surname:String,
    val email:String,
    val profilePicture:String,
    val stars:Double

    )