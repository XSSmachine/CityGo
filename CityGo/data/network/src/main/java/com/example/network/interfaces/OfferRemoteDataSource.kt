package com.example.network.interfaces

import com.hfad.model.OfferRequestModel
import com.hfad.model.OfferResponseModel
import com.hfad.model.RepoResult
import com.hfad.model.ResponseBody

interface OfferRemoteDataSource {
    suspend fun getAll(): RepoResult<List<OfferResponseModel>>

//    suspend fun getAllForCurrentUser(serviceProviderId: String): RepoResult<List<OfferResponseModel>>
    // I will get offer list from remote provider entity and then use that ids to get each
    // offer entity individually and then save it in list and display it to the provider

//    suspend fun getAllForCurrentRequest(sid :String)
        // I will get offer list from one user request and then get them
        //  individually and save to list

    suspend fun getOne(sid:String): RepoResult<OfferResponseModel>

    suspend fun delete(sid:String):RepoResult<Unit>

    suspend fun update(sid:String, offer: OfferRequestModel):RepoResult<Unit>

    suspend fun updateStatus(sid:String, status: Map<String,String>):RepoResult<Unit>

    suspend fun create(offer: OfferRequestModel):RepoResult<ResponseBody>

}