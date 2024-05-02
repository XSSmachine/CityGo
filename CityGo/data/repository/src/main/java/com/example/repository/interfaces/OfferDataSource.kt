package com.example.repository.interfaces

import com.hfad.model.OfferRequestModel
import com.hfad.model.OfferResponseModel
import com.hfad.model.UserRequestRequestModel
import com.hfad.model.UserRequestResponseModel

interface OfferDataSource {
    suspend fun getAll(userRequestId:Int): List<OfferResponseModel>

    suspend fun getAllForCurrentUser(serviceProviderId: String): List<OfferResponseModel>

    suspend fun getOne(userRequestId:Int, serviceProviderId:String): OfferResponseModel?

    suspend fun delete(userRequestId:Int, serviceProviderId:String)

    suspend fun update(userRequestId:Int, serviceProviderId:String, offer: OfferRequestModel)

    suspend fun updateStatus(userRequestId:Int, serviceProviderId:String,status:String)

    suspend fun create(offer: OfferRequestModel)

    suspend fun doesOfferExist(userRequestId: Int,serviceProviderId: String):Int
}