package com.example.network.entities

import android.net.Uri
import com.hfad.model.Address
import com.hfad.model.UserRequestRequestModel
import com.hfad.model.UserRequestResponseModel

data class UserRequestRemoteEntity(
    val id: Int? = null,
    val userId: String,
    val photo: String,
    val description: String,
    val address1: Address,
    val address2: Address,
    val timeTable: String,
    val date:String,
    val category: String,
    val extraWorker: Boolean,
    val price: Int
)

/**
 * Mapping functions
 * @author Karlo Kovačević
 */

//Useful when retrieving data from the database and
// mapping it to a model for presentation or usage in the UI
fun UserRequestRemoteEntity.toUserRequestResponseModel(): UserRequestResponseModel {
    return UserRequestResponseModel(
        id = id!!,
        userId =userId,
        photo = Uri.parse(photo),
        description =description,
        address1 = address1,
        address2 = address2,
        timeTable = timeTable,
        date=date,
        category = category,
        extraWorker =extraWorker,
        price = price,
    )
}

//This method is useful when preparing data to be stored in the database
fun UserRequestRequestModel.toUserRequestRoomEntity(): UserRequestRemoteEntity{
    return UserRequestRemoteEntity(
        id = id,
        userId=userId,
        photo=photo,
        description=description,
        address1 = address1,
        address2 = address2,
        timeTable=timeTable,
        date=date,
        category=category,
        extraWorker=extraWorker,
        price=price,
    )
}