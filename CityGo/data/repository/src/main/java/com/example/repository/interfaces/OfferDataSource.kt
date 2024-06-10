package com.example.repository.interfaces

import com.hfad.model.OfferRequestModel
import com.hfad.model.OfferResponseModel
import com.hfad.model.RepoResult
import com.hfad.model.ServiceProviderProfileResponseModel
import com.hfad.model.UserRequestRequestModel
import com.hfad.model.UserRequestResponseModel
import kotlinx.coroutines.flow.Flow

interface NullableOfferModel{
    val model : ServiceProviderProfileResponseModel?
}
interface OfferDataSource {
    suspend fun getAll(userRequestUIID:String): RepoResult<List<OfferResponseModel>>

    suspend fun getAllForCurrentUser(serviceProviderId: String): RepoResult<List<OfferResponseModel>>

    suspend fun getOne(userRequestUIID:String, serviceProviderId:String): RepoResult<OfferResponseModel>

    suspend fun delete(userRequestUIID:String, serviceProviderId:String):RepoResult<Unit>

    suspend fun update(userRequestUIID:String, serviceProviderId:String, offer: OfferRequestModel):RepoResult<Unit>

    suspend fun updateStatus(userRequestUIID:String, serviceProviderId:String,status:String):RepoResult<Unit>

    suspend fun create(offer: OfferRequestModel):RepoResult<Unit>

    suspend fun doesOfferExist(userRequestUIID:String,serviceProviderId: String):RepoResult<Int>
}