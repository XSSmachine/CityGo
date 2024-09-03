package com.example.network.entities

import android.net.Uri
import com.google.gson.annotations.SerializedName
import com.hfad.model.Address
import com.hfad.model.UserRequestRequestModel
import com.hfad.model.UserRequestResponseModel

data class UserRequestRemoteEntity(
    @SerializedName("ID")
    val uuid: String?=null,
    @SerializedName("UserID")
    val userId: String,
    @SerializedName("Photo")
    val photo: String?,
    @SerializedName("Description")
    val description: String,
    @SerializedName("Address1")
    val address1: RemoteAddress,
    @SerializedName("Address2")
    val address2: RemoteAddress,
    @SerializedName("TimeTable")
    val timeTable: String,
    @SerializedName("Date")
    val date:String,
    @SerializedName("Category")
    val category: String,
    @SerializedName("ExtraWorker")
    val extraWorker: Boolean,
    @SerializedName("Price")
    val price: Int,
    @SerializedName("SID")
    val sid: String?=null,
    @SerializedName("Sync")
    val sync:Long?=null,
    @SerializedName("Offers")
    val offers: List<String>? = emptyList()
)

data class RemoteAddress(
    @SerializedName("AddressName")
    val addressName: String?,
    @SerializedName("Latitude")
    val latitude: Double?,
    @SerializedName("Longitude")
    val longitude: Double?,
    @SerializedName("LiftStairs")
    val liftStairs: Boolean,
    @SerializedName("Floor")
    val floor: Int,
    @SerializedName("DoorCode")
    val doorCode: String?, // Optional
    @SerializedName("PhoneNumber")
    val phoneNumber: String?,
)




/**
 * Mapping functions
 * @author Karlo Kovačević
 */


fun RemoteAddress.toAddress(): Address {
    return Address(
        addressName = this.addressName ?: "",
        latitude = this.latitude,
        longitude = this.longitude,
        liftStairs = this.liftStairs,
        floor = this.floor,
        doorCode = this.doorCode,
        phoneNumber = this.phoneNumber ?: "",
    )
}

fun Address.toRemoteAddress(): RemoteAddress {
    return RemoteAddress(
        addressName = this.addressName,
        latitude = this.latitude,
        longitude = this.longitude,
        liftStairs = this.liftStairs,
        floor = this.floor,
        doorCode = this.doorCode,
        phoneNumber = this.phoneNumber
    )
}

//Useful when retrieving data from the database and
// mapping it to a model for presentation or usage in the UI
fun UserRequestRemoteEntity.toUserRequestResponseModel(): UserRequestResponseModel {
    return UserRequestResponseModel(
        uuid = uuid ?: "",
        userId =userId,
        photo = Uri.parse(photo),
        description =description,
        address1 = address1.toAddress(),
        address2 = address2.toAddress(),
        timeTable = timeTable,
        date=date,
        category = category,
        extraWorker =extraWorker,
        price = price,
        sid = sid,
        sync = sync,
        offers=offers

    )
}

//This method is useful when preparing data to be stored in the database
fun UserRequestRequestModel.toUserRequestRemoteEntity(): UserRequestRemoteEntity{
    return UserRequestRemoteEntity(
        uuid = uuid ?: "",
        userId=userId,
        photo=photo,
        description=description,
        address1 = address1.toRemoteAddress(),
        address2 = address2.toRemoteAddress(),
        timeTable=timeTable,
        date=date,
        category=category,
        extraWorker=extraWorker,
        price=price,
        sid=sid,
        sync=sync,
        offers = offers ?: emptyList()

    )
}