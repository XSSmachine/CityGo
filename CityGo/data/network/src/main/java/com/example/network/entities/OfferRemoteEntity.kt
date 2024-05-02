package com.example.network.entities

import com.hfad.model.OfferRequestModel
import com.hfad.model.OfferResponseModel

data class OfferRemoteEntity(
    val id: Int? = null,
    val userRequestId: Int,
    val serviceProviderId: String,
    val price: Int?,
    val timeTable: String?,
    val status: String
)

fun OfferRemoteEntity.toOfferResponseModel(): OfferResponseModel {
    return OfferResponseModel(
        id = id!!,
        userRequestId =userRequestId,
        serviceProviderId = serviceProviderId,
        price =price,
        timeTable = timeTable,
        status = status,

        )
}

//This method is useful when preparing data to be stored in the database
fun OfferRequestModel.toOfferRemoteEntity(): OfferRemoteEntity{
    return OfferRemoteEntity(
        id = id,
        userRequestId =userRequestId,
        serviceProviderId = serviceProviderId,
        price =price,
        timeTable = timeTable,
        status = status,
    )
}
