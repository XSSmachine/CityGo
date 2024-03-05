package com.example.userrequest.create

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.domain.interfaces.usecases.CreateUserRequestUseCase
import com.hfad.model.Address
import com.hfad.model.UserRequestRequestModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateUserRequestViewModel @Inject constructor(
    private val createUserRequestUseCase: CreateUserRequestUseCase
) : ViewModel() {


    private val _errorMessage = mutableStateOf("")

    private val _address1 = mutableStateOf(Address("",0.0,0.0,true,0,"",""))
    private val _address2 = mutableStateOf(Address("",0.0,0.0,true,0,"",""))
    private val _addressName = mutableStateOf("")
    private val _latitude = mutableDoubleStateOf(0.0)
    private val _longitude = mutableDoubleStateOf(0.0)
    private val _liftStairs = mutableStateOf("")
    private val _floors = mutableIntStateOf(0)
    private val _doorCode = mutableStateOf("")
    private val _phoneNumber = mutableStateOf("")

    private val _description = mutableStateOf("")
    private val _capturedImageUris = mutableStateOf("")
    var selectedChip by mutableStateOf("")
    private val _timeTable = mutableStateOf("")
    private val _category = mutableStateOf("")
    private val _extraWorker = mutableStateOf("")
    private val _price = mutableIntStateOf(0)

    // Address 1
    val addressName1 = mutableStateOf("")
    val latitude1 = mutableStateOf(0.0)
    val longitude1 = mutableStateOf(0.0)
    val liftStairs1 = mutableStateOf(true)
    val floors1 = mutableStateOf(0)
    val doorCode1 = mutableStateOf("")
    val phoneNumber1 = mutableStateOf("")

    // Address 2
    val addressName2 = mutableStateOf("")
    val latitude2 = mutableStateOf(0.0)
    val longitude2 = mutableStateOf(0.0)
    val liftStairs2 = mutableStateOf(true)
    val floors2 = mutableStateOf(0)
    val doorCode2 = mutableStateOf("")
    val phoneNumber2 = mutableStateOf("")

    // Methods for Address 1
    fun setAddressName1(value: String) {
        addressName1.value = value
    }

    fun setLatitude1(value: Double) {
        latitude1.value = value
    }

    fun setLongitude1(value: Double) {
        longitude1.value = value
    }

    fun setLiftStairs1(value: Boolean) {
        liftStairs1.value = value
    }

    fun setFloors1(value: Int) {
        floors1.value = value
    }

    fun setDoorCode1(value: String) {
        doorCode1.value = value
    }

    fun setPhoneNumber1(value: String) {
        phoneNumber1.value = value
    }

    // Methods for Address 2
    fun setAddressName2(value: String) {
        addressName2.value = value
    }

    fun setLatitude2(value: Double) {
        latitude2.value = value
    }

    fun setLongitude2(value: Double) {
        longitude2.value = value
    }

    fun setLiftStairs2(value: Boolean) {
        liftStairs2.value = value
    }

    fun setFloors2(value: Int) {
        floors2.value = value
    }

    fun setDoorCode2(value: String) {
        doorCode2.value = value
    }

    fun setPhoneNumber2(value: String) {
        phoneNumber2.value = value
    }


    val address1 : Address
        get() = Address(
            addressName = _address1.value.addressName,
            latitude  = _address1.value.latitude,
            longitude = _address1.value.longitude,
            doorCode = _address1.value.doorCode,
            phoneNumber = _address1.value.phoneNumber,
            liftStairs = _address1.value.liftStairs,
            floor =  _address1.value.floor
        )

    val address2 : Address
        get() = Address(
            addressName = _address1.value.addressName,
            latitude  = _address1.value.latitude,
            longitude = _address1.value.longitude,
            doorCode = _address1.value.doorCode,
            phoneNumber = _address1.value.phoneNumber,
            liftStairs = _address1.value.liftStairs,
            floor =  _address1.value.floor
        )

    val description : String
        get() = _description.value

    val addressName: String
        get() = _addressName.value

    val latitude: Double
        get() = _latitude.value

    val longitude: Double
        get() = _longitude.value

    val liftStairs: Boolean
        get() = _liftStairs.value.toBoolean()

    val floors: Int
        get() = _floors.value

    val doorCode: String
        get() = _doorCode.value

    val phoneNumber: String
        get() = _phoneNumber.value

    val photos : String
        get() = _capturedImageUris.value

    val errorMessage : String
        get() = _errorMessage.value

    val timeTable : String
        get() = _timeTable.value

    val category : String
        get() = _category.value

    val extraWorker : Boolean
        get() = _extraWorker.value.toBoolean()

    val price : Int
        get() = _price.value


    fun onImageCaptured(uri: Uri) {
        _capturedImageUris.value = uri.toString()
    }

    fun onDescriptionChange(newName: String) {
        _description.value=newName
    }

    fun setAddressName(addressName: String) {
        _addressName.value = addressName
    }

    fun setLatitude(latitude: Double?) {
        _latitude.value = latitude ?: 0.0 // Default to 0.0 if latitude is null
    }

    fun setLongitude(longitude: Double?) {
        _longitude.value = longitude ?: 0.0 // Default to 0.0 if longitude is null
    }

    fun setLiftStairs(liftStairs: Boolean) {
        _liftStairs.value = liftStairs.toString()
    }

    fun setPrice(price: Int) {
        _price.value = price
    }

    fun setDoorCode(doorCode: String?) {
        _doorCode.value = doorCode ?: ""
    }

    fun setPhoneNumber(phoneNumber: String) {
        _phoneNumber.value = phoneNumber
    }

    fun setAddress1(address : Address){
        _address1.value = address
    }

    fun setAddress2(address : Address){
        _address2.value = address
    }

    fun setTimeTable(time : String){
        _timeTable.value = time
    }

    fun saveAddresses() {
        // Create Address objects using the saved attributes and perform necessary actions
        _address1.value = Address(
            addressName = addressName1.value,
            latitude = latitude1.value,
            longitude = longitude1.value,
            liftStairs = liftStairs1.value,
            floor = floors1.value,
            doorCode = doorCode1.value,
            phoneNumber = phoneNumber1.value
        )

        _address2.value = Address(
            addressName = addressName2.value,
            latitude = latitude2.value,
            longitude = longitude2.value,
            liftStairs = liftStairs2.value,
            floor = floors2.value,
            doorCode = doorCode2.value,
            phoneNumber = phoneNumber2.value
        )

        // Perform necessary actions with the addresses, such as saving them to a list
    }

    suspend fun createContact() {
        try {
            createUserRequestUseCase.execute(
                UserRequestRequestModel(
                id=null, userId = "test1", photo=_capturedImageUris.value,address1=_address1.value, address2=_address2.value,description = _description.value, timeTable = _timeTable.value, category = "L", extraWorker = _extraWorker.value.toBoolean(), price = _price.value
            ))
        } catch (e: Exception) {
            _errorMessage.value = "Error ${e.message}"
        }
    }
}