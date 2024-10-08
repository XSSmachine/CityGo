package com.example.ui_users.read

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.interfaces.serviceproviderprofile_usecases.CreateServiceProviderProfileUseCase
import com.example.domain.interfaces.serviceproviderprofile_usecases.DeleteServiceProviderProfileUseCase
import com.example.domain.interfaces.serviceproviderprofile_usecases.GetServiceProviderProfileUseCase
import com.example.domain.interfaces.serviceproviderprofile_usecases.UpdateServiceProviderProfileUseCase
import com.example.domain.interfaces.serviceproviderprofile_usecases.UpdateServiceProviderStatusUseCase
import com.example.domain.interfaces.userprofile_usecases.ClearUserIdUseCase
import com.example.domain.interfaces.userprofile_usecases.CreateUserProfileUseCase
import com.example.domain.interfaces.userprofile_usecases.GetUserIdUseCase
import com.example.domain.interfaces.userprofile_usecases.GetUserProfileUseCase
import com.example.domain.interfaces.userprofile_usecases.SetUserRoleUseCase
import com.example.domain.interfaces.userprofile_usecases.UpdateUserProfileUseCase
import com.google.firebase.auth.FirebaseAuth
import com.hfad.model.BasicError
import com.hfad.model.Error
import com.hfad.model.Failure
import com.hfad.model.RepoResult
import com.hfad.model.ServiceProviderProfileRequestModel
import com.hfad.model.ServiceProviderProfileResponseModel
import com.hfad.model.Success
import com.hfad.model.Triumph
import com.hfad.model.UserProfileRequestModel
import com.hfad.model.UserProfileResponseModel
import com.hfad.model.ViewState
import com.hfad.model.onFailure
import com.hfad.model.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val clearUserIdUseCase: ClearUserIdUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val createServiceProviderProfileUseCase: CreateServiceProviderProfileUseCase,
    private val updateServiceProviderStatusUseCase: UpdateServiceProviderStatusUseCase,
    private val getServiceProviderStatusUseCase: GetServiceProviderProfileUseCase,
    private val deleteServiceProviderProfileUseCase: DeleteServiceProviderProfileUseCase,
    private val setUserRoleUseCase: SetUserRoleUseCase
    ): ViewModel(){

    //user profile data
//    val _statusLiveData = mutableStateOf("")

      private val _tabIndex = mutableStateOf(0)
    private val _profilePicture = mutableStateOf("")
    private val _name = mutableStateOf("")
    private val _surname = mutableStateOf("")
    private val _email = mutableStateOf("")

    //service provider data
    private val sp_name = mutableStateOf("")
    private val sp_surname = mutableStateOf("")
    private val sp_email = mutableStateOf("")
    private val _dateOfBirth = mutableStateOf("")
    private val _address = mutableStateOf("")
    private val _zipCode = mutableStateOf("")
    private val _city = mutableStateOf("")
    private val _country = mutableStateOf("")
    private val _selfiePicture = mutableStateOf<Uri?>(null)
    private val _vehiclePicture = mutableStateOf<Uri?>(null)
    private val _idCardFrontPicture = mutableStateOf<Uri?>(null)
    private val _idCardBackPicture = mutableStateOf<Uri?>(null)

    protected val _viewState = MutableLiveData<ViewState<UserProfileResponseModel>>()
    val viewState: LiveData<ViewState<UserProfileResponseModel>>
        get() = _viewState

    fun updateUserData(newData: ViewState<UserProfileResponseModel>) {
        _viewState.value = newData
    }

    protected val _providerViewState = MutableLiveData<ViewState<ServiceProviderProfileResponseModel>>()
    val providerViewState: LiveData<ViewState<ServiceProviderProfileResponseModel>>
        get() = _providerViewState

    var state by mutableStateOf(ProfileState())

    init {
        viewModelScope.launch {
            getProfileDetails()
            refreshStatus()
            getServiceProviderProfileDetails()
        }

    }
    private val _statusLiveData = MutableStateFlow("")
    val statusLiveData: StateFlow<String> = _statusLiveData.asStateFlow()

//    fun refreshStatus() {
//        viewModelScope.launch {
//            val newStatus = repository.fetchUserStatus()
//            _statusLiveData.value = newStatus
//        }
//    }

    fun setStatusLiveData(status: String) {
        viewModelScope.launch {
            _statusLiveData.value = status
        }
    }

    val tabIndex: Int
        get() = _tabIndex.value

    fun setTabIndex(value: Int) {
        _tabIndex.value = value
    }

    val spname: String
        get() = sp_name.value

    fun setSPName(value: String) {
        sp_name.value = value
    }

    val spsurname: String
        get() = sp_surname.value

    fun setSPSurame(value: String) {
        sp_surname.value = value
    }

    val spemail: String
        get() = sp_email.value

    fun setSPEmail(value: String) {
        sp_email.value = value
    }

    val dateOfBirth: String
        get() = _dateOfBirth.value

    fun setDateOfBirth(value: String) {
        _dateOfBirth.value = value
    }

    val address: String
        get() = _address.value

    fun setAddress(value: String) {
        _address.value = value
    }

    val zipCode: String
        get() = _zipCode.value

    fun setZipCode(value: String) {
        _zipCode.value = value
    }

    val city: String
        get() = _city.value

    fun setCity(value: String) {
        _city.value = value
    }

    val country: String
        get() = _country.value

    fun setCountry(value: String) {
        _country.value = value
    }

    val selfiePicture: Uri?
        get() = _selfiePicture.value

    fun setSelfiePicture(value: Uri?) {
        _selfiePicture.value = value
    }

    val idCardFrontPicture: Uri?
        get() = _idCardFrontPicture.value

    fun setIdCardFrontPicture(value: Uri?) {
        _idCardFrontPicture.value = value
    }

    val idCardBackPicture: Uri?
        get() = _idCardBackPicture.value

    fun setIdCardBackPicture(value: Uri?) {
        _idCardBackPicture.value = value
    }

    val vehiclePicture: Uri?
        get() = _vehiclePicture.value

    fun setVehiclePicture(value: Uri?) {
        _vehiclePicture.value = value
    }


    //------------------------------------------------------------------------------
    val profilePicture: String
        get() = _profilePicture.value

    fun setProfilePicture(value: String) {
        _profilePicture.value = value
    }
    //-----------------------------
    val name: String
        get() = _name.value

    fun setName(value: String) {
        _name.value = value
    }
    //-----------------------------
    val surname: String
        get() = _surname.value

    fun setSurname(value: String) {
        _surname.value = value
    }

    //-----------------------------
    val email: String
        get() = _email.value

    fun setEmail(value: String) {
        _email.value = value
    }


    suspend fun updateUserProfile(data:UserProfileResponseModel){
        viewModelScope.launch {
            val user = UserProfileRequestModel(
                getIdValue(),
                name=data.name,
                surname=data.surname,
                email=data.email,
                phoneNumber = "",
                profilePicture=data.profilePicture!!,
                stars = 4.00,
                sid = null,
                sync = null,
                requests = emptyList()
            )
            updateUserProfileUseCase.execute(getIdValue(),user
                ).onSuccess { _viewState.postValue(Triumph(user.toResponseModel())) }
        }
    }



    fun deleteServiceProviderApplication(){
        viewModelScope.launch {
            deleteServiceProviderProfileUseCase.execute(getIdValue())
        }
    }


//    fun getServiceProviderApplicationStatus(): String {
//        viewModelScope.launch {
//            try {
//                getServiceProviderStatusUseCase.execute(getIdValue())
//                    .onSuccess { setStatusLiveData(it.status)}
//                    .onFailure { setStatusLiveData("") }
//            }catch (e: Exception){
//                Failure(BasicError(Throwable(e)))
//            }
//        }
//
//        return statusLiveData
//    }


    suspend fun createServiceProviderProfile(){
        viewModelScope.launch {
            createServiceProviderProfileUseCase.execute(
                ServiceProviderProfileRequestModel(
                    id = state.id,
                    name = spname,
                    surname = spsurname,
                    email = spemail,
                    dateOfBirth = dateOfBirth,
                    address = address,
                    latitude = null,
                    longitude = null,
                    zipCode = zipCode,
                    city = city,
                    country = country,
                    phoneNumber = state.phoneNum,
                    profilePicture = selfiePicture.toString(),
                    idPictureFront = idCardFrontPicture.toString(),
                    idPictureBack = idCardBackPicture.toString(),
                    vehiclePicture = vehiclePicture.toString(),
                    stars = state.stars,
                    status = "Pending",
                    sid = null,
                    sync = null,
                    offers = null
                )
            )
        }
    }

    suspend fun updateServiceProviderStatus(status:String){
        viewModelScope.launch { updateServiceProviderStatusUseCase.execute(getIdValue(),status) }

    }

    fun setProfileValues(state: ProfileState){
        setName(state.name)
        setSurname(state.surname)
        setEmail(state.email)
        setProfilePicture(state.profilePicture)
    }

    @SuppressLint("SuspiciousIndentation")
    suspend fun getProfileDetails(){

            getUserProfileUseCase.execute(getIdValue())
                .onSuccess {
                    state = state.copy(
                        id = it.id,
                        name=it.name,
                        surname=it.surname,
                        email=it.email.orEmpty(),
                        stars = it.stars,
                        phoneNum = it.phoneNumber,
                        profilePicture = it.profilePicture.toString()

                    )
                    _viewState.postValue(Triumph(it))

                }
                .onFailure { _viewState.postValue(Error(it,"user details")) }

//        val user = getUserProfileUseCase.execute(UserId)
//        if (user!=null)

//        setProfileValues(state)

    }

    @SuppressLint("SuspiciousIndentation")
    suspend fun getServiceProviderProfileDetails(){
        getServiceProviderStatusUseCase.execute(getIdValue())
            .onSuccess { _providerViewState.postValue(Triumph(it)) }
            .onFailure { _providerViewState.postValue(Error(it,"provider details error")) }

//        val user = getServiceProviderStatusUseCase.execute(UserId)
//        if (user!=null)
//            providerState = providerState.copy(
//                id = user.id,
//                name=user.name,
//                surname=user.surname,
//                email=user.email,
//                stars = user.stars,
//                dateOfBirth=user.dateOfBirth,
//                address=user.address,
//                profilePicture=user.vehiclePicture,
//
//
//            )
    }


    //Manipulate current user identification


    fun setUserRole(userRole:String){
        viewModelScope.launch { setUserRoleUseCase.execute(userRole) }

    }
    suspend fun clearUserId(){
        clearUserIdUseCase.execute()
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

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    fun refreshStatus() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                val newStatus = getServiceProviderStatusUseCase.execute(getIdValue())
                 newStatus.onSuccess { _statusLiveData.value =it.status }
            } catch (e: Exception) {
                // Handle error
                Log.e("UserProfileViewModel", "Error refreshing status", e)
                // Optionally update UI to show error
            } finally {
                _isRefreshing.value = false
            }
        }
    }

}

//Create a file for a camera picture
fun Context.createImageFile(): File {

    val timestamp = SimpleDateFormat("yyyy_MM_dd_HH-mm-ss").format(Date())
    val imageFileName = "image_"+timestamp+"_"

    return File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )


}
/*
Data classes used for holding data
 */

data class ProfileState(
    val id:String="",
    val name:String="",
    val surname:String="",
    val email:String="",
    val phoneNum:String="",
    val profilePicture: String="",
    val stars:Double=0.0,

    )
data class UpdateProfileState(
    val name:String="",
    val surname:String="",
    val email:String="",
    val profilePicture: String="",

    )
data class ServiceProviderProfileState(
    val id:String="",
    val name:String="",
    val surname:String="",
    val email:String="",
    val dateOfBirth:String="",
    val address:String="",
    val profilePicture:String="",
    val stars:Double=0.0,

    )

fun ServiceProviderProfileResponseModel.toServiceProviderProfileState(): ServiceProviderProfileState {
    return ServiceProviderProfileState(
        id = id,
        name = name,
        surname = surname,
        email = email,
        dateOfBirth = dateOfBirth,
        address = address,
        profilePicture = profilePicture,
        stars = stars
    )
}

fun UserProfileRequestModel.toResponseModel(): UserProfileResponseModel {
    return UserProfileResponseModel(
        id = this.id,
        name = this.name,
        surname = this.surname,
        email = this.email,
        phoneNumber = this.phoneNumber,
        profilePicture = this.profilePicture,
        stars = this.stars,
        sid = this.sid,
        sync = this.sync,
        requests = this.requests
    )
}

