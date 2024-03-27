package com.example.userrequest.read

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import com.example.domain.interfaces.userrequest_usecases.GetAllUserRequestsUseCase
import com.hfad.model.UserRequestResponseModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class UserRequestListResponseModel(
    val id: String,
    val address1: String,
    val address2: String,
    val price: Int,
    val imageUri: Uri
)

fun UserRequestResponseModel.toContactListResponseModel()
        :UserRequestListResponseModel {
    return UserRequestListResponseModel(
        id = id.toString(),
        address1 = address1.toString(),
        address2 = address1.toString(),
        price=price,
        imageUri=photo.toUri(),

        )
}

@HiltViewModel
class ReadUserRequestViewModel @Inject constructor(
    private val getAllContactsUseCase: GetAllUserRequestsUseCase
) : ViewModel() {

    private val _errorMessage = mutableStateOf("")
    private val _contacts = mutableStateListOf<UserRequestListResponseModel>()

    val errorMessage : String
        get() = _errorMessage.value

    val contacts: List<UserRequestListResponseModel>
        get() = _contacts.toList()

    suspend fun getContacts() {
        try {
            _contacts.clear()
            val list = getAllContactsUseCase.execute(1)
            _contacts.addAll(list.map {
                it.toContactListResponseModel()
            })
        }catch (e: Exception) {
            _errorMessage.value = "Greška ${e.message}"
        }
    }
}