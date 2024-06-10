package com.example.userrequest.create

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.interfaces.userprofile_usecases.GetUserIdUseCase
import com.example.domain.interfaces.userrequest_usecases.CreateUserRequestUseCase
import com.hfad.model.Address
import com.hfad.model.UserRequestRequestModel
import com.hfad.model.onFailure
import com.hfad.model.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

@HiltViewModel
class CreateUserRequestViewModel @Inject constructor(
    private val createUserRequestUseCase: CreateUserRequestUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,

) : ViewModel() {


    private val _errorMessage = mutableStateOf("")

    val _capturedImageUri  =
        mutableStateOf<Uri>(Uri.parse("android.resource://CityGo/presentation/ui-userrequests/src/main/res/drawable/baseline_image_24"))


    private val _address1 = mutableStateOf(Address("",0.0,0.0,true,0,"",""))
    private val _address2 = mutableStateOf(Address("",0.0,0.0,true,0,"",""))
    private val _addressName = mutableStateOf("")
    private val _latitude = mutableDoubleStateOf(0.0)
    private val _longitude = mutableDoubleStateOf(0.0)
    private val _liftStairs = mutableStateOf(0)
    private val _floors = mutableIntStateOf(0)
    private val _doorCode = mutableStateOf("")
    private val _phoneNumber = mutableStateOf("")

    private val _description = mutableStateOf("")
    private val _capturedImageUris = mutableStateOf("")
    var selectedChip by mutableStateOf("")
    private val _timeTable = mutableStateOf("")
    private val _category = mutableStateOf("")
    private val _extraWorker = mutableStateOf(0)
    private val _price = mutableIntStateOf(0)

    // Address 1
    val addressName1 = mutableStateOf("")
    val latitude1 = mutableStateOf(0.0)
    val longitude1 = mutableStateOf(0.0)
    val liftStairs1 = mutableStateOf(0)
    val floors1 = mutableStateOf("0")
    val doorCode1 = mutableStateOf("")
    val phoneNumber1 = mutableStateOf("")

    // Address 2
    val addressName2 = mutableStateOf("")
    val latitude2 = mutableStateOf(0.0)
    val longitude2 = mutableStateOf(0.0)
    val liftStairs2 = mutableStateOf(0)
    val floors2 = mutableStateOf("0")
    val doorCode2 = mutableStateOf("")
    val phoneNumber2 = mutableStateOf("")


    private var clickCounter: Int by mutableStateOf(0)
    fun incrementClickCounter() {
        clickCounter++
    }

    // Function to decrement the click counter
    fun decrementClickCounter() {
        clickCounter--
    }

    val capturedImageUri:Uri
        get() = _capturedImageUri.value

    fun setCapturedImageUri(value: Uri) {
        _capturedImageUri.value = value
    }

    val category:String
        get() = _category.value

    fun setCategory(value: String) {
        _category.value = value
    }

    fun getClickCount(): Int {
        return clickCounter
        clickCounter=0
    }
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

    fun setLiftStairs1(value: Int) {
        liftStairs1.value = value
    }

    fun setFloors1(value: String) {
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

    fun setLiftStairs2(value: Int) {
        liftStairs2.value = value
    }

    fun setFloors2(value: String) {
        floors2.value = value
    }

    fun setDoorCode2(value: String) {
        doorCode2.value = value
    }

    fun setPhoneNumber2(value: String) {
        phoneNumber2.value = value
    }


    fun resetValues() {
        // Reset Address 1
        _address1.value = Address("", 0.0, 0.0, true, 0, "", "")
        addressName1.value = ""
        latitude1.value = 0.0
        longitude1.value = 0.0
        liftStairs1.value = 0
        floors1.value = "0"
        doorCode1.value = ""
        phoneNumber1.value = ""

        // Reset Address 2
        _address2.value = Address("", 0.0, 0.0, true, 0, "", "")
        addressName2.value = ""
        latitude2.value = 0.0
        longitude2.value = 0.0
        liftStairs2.value = 0
        floors2.value = "0"
        doorCode2.value = ""
        phoneNumber2.value = ""

        // Reset other values
        _description.value = ""
        _capturedImageUris.value = ""
        selectedChip = ""
        _timeTable.value = ""
        _category.value = ""
        _extraWorker.value = 0
        _price.value = 0
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

    val extraWorker : Int
        get() = _extraWorker.value

    fun setExtraWorker(value:Int) {
        _extraWorker.value = value
    }

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

    fun setLiftStairs(liftStairs: Int) {
        _liftStairs.value = liftStairs
    }

    fun setPrice(price: Int) {
        _price.value = price
    }

    fun Int.add5(): Int {
        return this + 5
    }

    fun Int.minus5(): Int {
        return this - 5
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
            liftStairs = liftStairs1.value==0,
            floor = floors1.value.toInt(),
            doorCode = doorCode1.value,
            phoneNumber = phoneNumber1.value
        )

        _address2.value = Address(
            addressName = addressName2.value,
            latitude = latitude2.value,
            longitude = longitude2.value,
            liftStairs = liftStairs2.value==0,
            floor = floors2.value.toInt(),
            doorCode = doorCode2.value,
            phoneNumber = phoneNumber2.value
        )

        // Perform necessary actions with the addresses, such as saving them to a list
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun generateTimeSlots(): List<String> {
        val timeSlots = mutableListOf<String>()

        // Get current date and time
        val currentDateTime = ZonedDateTime.now(ZoneId.of("Europe/Zagreb"))


        // Get current date and next day
        val currentDate = currentDateTime.toLocalDate()
        val nextDate = currentDate.plusDays(1)

        var currentHour = currentDateTime.hour
        Log.d("HOUR",currentHour.toString())
        var currentMinute = currentDateTime.minute
        Log.d("MINUTE",currentMinute.toString())

        // If the current minute is more than 30, round up the current hour
        if (currentMinute > 30) {
            currentHour += 1
        }

        // Calculate the next hour from the current time
        var nextHour = currentHour

        // Generate time slots for the rest of today
        while (nextHour < 24) {
            val slotStartHour = nextHour
            val slotEndHour = nextHour + 1

            val slot = "${slotStartHour.toString().padStart(2, '0')} - ${slotEndHour.toString().padStart(2, '0')}, ${currentDate.format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy"))}"
            timeSlots.add(slot)

            nextHour++
        }

        // Generate time slots for the entire day tomorrow
        nextHour = 0 // Reset next hour to 0 for tomorrow

        while (nextHour < 24) {
            val slotStartHour = nextHour
            val slotEndHour = nextHour + 1

            val slot = "${slotStartHour.toString().padStart(2, '0')} - ${slotEndHour.toString().padStart(2, '0')}, ${nextDate.format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy"))}"
            timeSlots.add(slot)

            nextHour++
        }

        return timeSlots
    }


//    fun generateTimeSlots(): List<String> {
//        val timeSlots = mutableListOf<String>()
//        val currentTime = Date()
//        val calendar = Calendar.getInstance()
//
//        val date = getCurrentDateTime()
//        val dateInString = date.toString("dd/MM/yyyy HH:mm:ss")
//
//        calendar.time = currentTime
//        var currentHour = calendar.get(Calendar.HOUR_OF_DAY)
//        val currentMinute = calendar.get(Calendar.MINUTE)
//
//        // If the current minute is more than 30, round up the current hour
//        if (currentMinute > 30) {
//            currentHour += 1
//        }
//
//        val startTime = currentHour + 1
//        val endTime = currentHour + 24 + (24-currentHour) // Generating time slots for the next 24 hours
//
//        for (i in startTime until endTime) {
//            val slotStartHour = i % 24 // Convert to 24-hour format
//            val slotEndHour = (i + 1) % 24 // Convert to 24-hour format
//            val dayLabel = if (i < 24) "today" else "tomorrow"
//            val slot = "${slotStartHour.toString().padStart(2, '0')} - ${slotEndHour.toString().padStart(2, '0')}, $dayLabel"
//            timeSlots.add(slot)
//        }
//
//        return timeSlots
//    }


    suspend fun getUserId(): Result<String?> {
        return getUserIdUseCase.execute()
    }


    fun getIdValue() : String{
        var r = ""
        runBlocking {
            r = getUserId().getOrNull()?:"none"
        }
        Log.d("VIEWMODEL-user_request",r)
        return r
    }


    fun createContact() {
        viewModelScope.launch { try {
            Log.d("TEST", _capturedImageUris.value)
            Log.d("TEST", _description.value)
            Log.d("TEST",getIdValue())
            val date = getCurrentDateTime()
            val dateInString = date.toString("dd/MM/yyyy HH:mm:ss")
            createUserRequestUseCase.execute(

//                UserRequestRequestModel(
//                    id=null, userId = "test1", photo="gallery/img1",address1=_address1.value, address2=_address2.value,description = "opis", timeTable = _timeTable.value, category = "L", extraWorker = _extraWorker.value, price = _price.value
//                ))
                UserRequestRequestModel(
                    uuid=null, userId = getIdValue(), photo=_capturedImageUris.value,address1=_address1.value, address2=_address2.value,description = _description.value, timeTable = _timeTable.value, date = dateInString, category = _category.value, extraWorker = _extraWorker.value==1, price = _price.value, sid = null, sync = null, offers = null
                )).onSuccess {  }
                .onFailure {  }
            resetValues()
        } catch (e: Exception) {
            _errorMessage.value = "Error ${e.message}"
        } }

    }
}

fun Context.createImageFile(): File {

    val timestamp = SimpleDateFormat("yyyy_MM_dd_HH-mm-ss").format(Date())
    val imageFileName = "image_"+timestamp+"_"

    return File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )


}


fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

fun getCurrentDateTime(): Date {
    return Calendar.getInstance().time
}