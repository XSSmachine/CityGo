package com.example.userrequest.update

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import com.example.domain.interfaces.userprofile_usecases.GetUserIdUseCase
import com.example.domain.interfaces.userrequest_usecases.GetUserRequestUseCase
import com.example.domain.interfaces.userrequest_usecases.UpdateUserRequestUseCase
import com.example.userrequest.create.getCurrentDateTime
import com.example.userrequest.create.toString
import com.hfad.model.Address
import com.hfad.model.UserRequestRequestModel
import com.hfad.model.UserRequestResponseModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class UpdateUserRequestViewModel @Inject constructor(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getUserRequestUseCase: GetUserRequestUseCase,
    private val updateUserRequestUseCase: UpdateUserRequestUseCase
) : ViewModel() {

    var state by mutableStateOf(UserRequestData())

    fun setExtraWorker(isExtraWorker: Boolean) {
        state = state.copy(extraWorker = isExtraWorker)
    }


    fun setUserRequestPicture(uri: Uri){
        state=state.copy(photo=uri)
    }
    fun onDescriptionChange(desc: String){
        state=state.copy(description = desc)
    }

    fun onPriceChange(price: Int?){
        state=state.copy(price = price!!)
    }


    fun setAddressName1(name: String) {
        // Update only the address1 field by copying the existing state and replacing the address1 field with the new name
        state = state.copy(address1 = state.address1.copy(addressName = name))
    }
    fun setAddressName2(name: String) {
        // Update only the address1 field by copying the existing state and replacing the address1 field with the new name
        state = state.copy(address2 = state.address2.copy(addressName = name))
    }

    fun setFloors1(floor: Int) {
        // Update only the address1 field by copying the existing state and replacing the address1 field with the new name
        state = state.copy(address1 = state.address1.copy(floor = floor))
    }
    fun setFloors2(floor: Int) {
        // Update only the address1 field by copying the existing state and replacing the address1 field with the new name
        state = state.copy(address2 = state.address2.copy(floor = floor))
    }

    fun setDoorCode1(code: String) {
        // Update only the address1 field by copying the existing state and replacing the address1 field with the new name
        state = state.copy(address1 = state.address1.copy(doorCode = code))
    }
    fun setDoorCode2(code: String) {
        // Update only the address1 field by copying the existing state and replacing the address1 field with the new name
        state = state.copy(address2 = state.address2.copy(doorCode = code))
    }

    fun setPhoneNumber1(num: String) {
        // Update only the address1 field by copying the existing state and replacing the address1 field with the new name
        state = state.copy(address1 = state.address1.copy(phoneNumber = num))
    }
    fun setPhoneNumber2(num: String) {
        // Update only the address1 field by copying the existing state and replacing the address1 field with the new name
        state = state.copy(address2 = state.address2.copy(phoneNumber = num))
    }






    // Function to fetch user request details
    suspend fun getUserRequest(requestId:Int,userId:String){
        val request= getUserRequestUseCase.execute(userId,requestId)
        Log.d("STATE",request!!.description)
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

    // Function to update user request details
    suspend fun updateUserRequest(requestId: Int,userId: String) {
        val date = getCurrentDateTime()
        val dateInString = date.toString("dd/MM/yyyy HH:mm:ss")
        state?.let { userRequest ->
            updateUserRequestUseCase.execute(userId, requestId,
                UserRequestRequestModel(
                    id=requestId,
                    userId=userId,
                    photo = state.photo.toString(),
                    description = state.description,
                    address1 = state.address1,
                    address2 = state.address2,
                    timeTable = state.timeTable,
                    category = state.category,
                    extraWorker = state.extraWorker,
                     date = dateInString,
                    price = state.price
                )
            )
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
}


data class UserRequestData(
    val photo: Uri ="".toUri(),
    val description: String="",
    val address1: Address = Address("",null,null,true,0,"",""),
    val address2: Address = Address("",null,null,true,0,"",""),
    val timeTable: String="",
    val category: String="",
    val extraWorker: Boolean=false,
    val price: Int=0
)
