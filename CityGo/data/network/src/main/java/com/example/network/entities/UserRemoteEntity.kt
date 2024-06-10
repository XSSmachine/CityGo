package com.example.network.entities

import com.google.gson.annotations.SerializedName
import com.hfad.model.UserProfileRequestModel
import com.hfad.model.UserProfileResponseModel
import java.io.Serial

data class UserRemoteEntity (
    @SerializedName("ID")
    val id: String,
    @SerializedName("Name")
    val name: String,
    @SerializedName("Surname")
    val surname: String,
    @SerializedName("Email")
    val email: String?,
    @SerializedName("PhoneNumber")
    val phoneNumber: String,
    @SerializedName("ProfilePicture")
    val profilePicture: String?,
    @SerializedName("Stars")
    val stars: Double,
    @SerializedName("SID")
    val sid: String?,
    @SerializedName("Sync")
    val sync:Long?,
    @SerializedName("Requests")
    val requests: List<String> = emptyList()
)

data class UserRemoteResponseEntity(
    val ID: String? = null,
    val Name: String? = null,
    val Surname: String? = null,
    val Email: String? = null,
    val PhoneNumber: String? = null,
    val ProfilePicture: String? = null,  // If it's not present in the response, remove or make it nullable
    val Stars: Double? = null,
    val Sync: Long? = null,
    val Requests: List<String>? = emptyList()
)

/**
 * Mapping functions
 * @author Karlo Kovačević
 */

//Useful when retrieving data from the database and
// mapping it to a model for presentation or usage in the UI
fun UserRemoteEntity.toUserProfileResponseModel(): UserProfileResponseModel {
    return UserProfileResponseModel(
        id = id,
        name=name,
        surname= surname,
        email=email,
        phoneNumber=phoneNumber,
        profilePicture=profilePicture,
        stars=stars,
        sid=sid,
        sync=sync,
        requests=requests
    )
}

//This method is useful when preparing data to be stored in the database
fun UserProfileRequestModel.toUserProfileRemoteEntity(): UserRemoteEntity{
    return UserRemoteEntity(
        id = id,
        name=name,
        surname= surname,
        email=email,
        phoneNumber=phoneNumber,
        profilePicture=profilePicture,
        stars=stars,
        sid=sid,
        sync=sync,
        requests=requests ?: emptyList()

    )
}
