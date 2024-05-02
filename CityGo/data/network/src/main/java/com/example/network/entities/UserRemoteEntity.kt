package com.example.network.entities

import com.hfad.model.UserProfileRequestModel
import com.hfad.model.UserProfileResponseModel

data class UserRemoteEntity (
val id: String,
val name: String,
val surname: String,
val email: String?,
val phoneNumber: String,
val profilePicture: String?,
val stars: Double
)

/**
 * Mapping functions
 * @author Karlo Kovačević
 */

//Useful when retrieving data from the database and
// mapping it to a model for presentation or usage in the UI
fun UserRemoteEntity.toUserProfileResponseModel(): UserProfileResponseModel {
    return UserProfileResponseModel(
        id = id!!,
        name=name,
        surname= surname,
        email=email,
        phoneNumber=phoneNumber,
        profilePicture=profilePicture,
        stars=stars,
    )
}

//This method is useful when preparing data to be stored in the database
fun UserProfileRequestModel.toUserProfileRoomEntity(): UserRemoteEntity{
    return UserRemoteEntity(
        id = id,
        name=name,
        surname= surname,
        email=email,
        phoneNumber=phoneNumber,
        profilePicture=profilePicture,
        stars=stars,
    )
}