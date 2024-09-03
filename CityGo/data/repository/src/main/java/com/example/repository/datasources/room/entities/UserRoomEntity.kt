package com.example.repository.datasources.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hfad.model.UserProfileRequestModel
import com.hfad.model.UserProfileResponseModel
import com.hfad.model.UserRequestRequestModel
import com.hfad.model.UserRequestResponseModel

/**
 * This class represents the structure of the "users" table in the database
 * @author Karlo Kovačević
 */
@Entity(tableName = "users")
data class UserProfileRoomEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val surname: String,
    val email: String?,
    val phoneNumber: String,
    val profilePicture: String?,
    val stars: Double,
    val sid: String? ,
    val sync:Long?
)

/**
 * Mapping functions
 * @author Karlo Kovačević
 */

//Useful when retrieving data from the database and
// mapping it to a model for presentation or usage in the UI
fun UserProfileRoomEntity.toUserProfileResponseModel(): UserProfileResponseModel {
    return UserProfileResponseModel(
        id = id!!,
        name=name,
        surname= surname,
        email=email,
        phoneNumber=phoneNumber,
        profilePicture=profilePicture,
        stars=stars,
        sid=sid,
        sync=sync,
        requests = null
    )
}

//This method is useful when preparing data to be stored in the database
fun UserProfileRequestModel.toUserProfileRoomEntity(): UserProfileRoomEntity{
    return UserProfileRoomEntity(
        id = id,
        name=name,
        surname= surname,
        email=email,
        phoneNumber=phoneNumber,
        profilePicture=profilePicture,
        stars=stars,
        sid=sid,
        sync=sync,
    )
}

fun UserProfileResponseModel.toUserProfileRequestModel(): UserProfileRequestModel{
    return UserProfileRequestModel(
        id = id,
        name=name,
        surname= surname,
        email=email,
        phoneNumber=phoneNumber,
        profilePicture=profilePicture!!,
        stars=stars,
        sid=sid,
        sync=sync,
        requests=requests
    )
}

