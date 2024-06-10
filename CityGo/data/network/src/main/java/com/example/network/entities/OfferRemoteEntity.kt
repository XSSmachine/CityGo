package com.example.network.entities

import com.google.gson.annotations.SerializedName
import com.hfad.model.OfferRequestModel
import com.hfad.model.OfferResponseModel

data class OfferRemoteEntity(
    @SerializedName("ID")
    val id: Int?,
    @SerializedName("UserRequestID")
    val userRequestUUID: String,
    @SerializedName("ServiceProviderID")
    val serviceProviderId: String,
    @SerializedName("Price")
    val price: Int?,
    @SerializedName("TimeTable")
    val timeTable: String?,
    @SerializedName("Status")
    val status: String,
    @SerializedName("SID")
    val sid: String?,
    @SerializedName("Sync")
    val sync:Long?
)

fun OfferRemoteEntity.toOfferResponseModel(): OfferResponseModel {
    return OfferResponseModel(
        id = id ?: 0,
        userRequestUUID =userRequestUUID,
        serviceProviderId = serviceProviderId,
        price =price,
        timeTable = timeTable,
        status = status,
        sid = sid,
        sync = sync

        )
}

//This method is useful when preparing data to be stored in the database
fun OfferRequestModel.toOfferRemoteEntity(): OfferRemoteEntity{
    return OfferRemoteEntity(
        id = id,
        userRequestUUID =userRequestUUID,
        serviceProviderId = serviceProviderId,
        price =price,
        timeTable = timeTable,
        status = status,
        sid=sid,
        sync=sync

    )
}
