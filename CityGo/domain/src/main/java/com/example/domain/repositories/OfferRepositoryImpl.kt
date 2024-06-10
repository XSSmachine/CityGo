package com.example.domain.repositories

import android.util.Log
import com.hfad.model.BasicError
import com.hfad.model.Failure
import com.hfad.model.RepoResult
import com.hfad.model.Success
import com.example.domain.interfaces.OfferRepository
import com.example.network.RemoteOfferDataSource
import com.example.network.RemoteProviderDataSource
import com.example.network.RemoteUserRequestDataSource
import com.example.repository.interfaces.OfferDataSource
import com.hfad.model.OfferRequestModel
import com.hfad.model.OfferResponseModel
import com.hfad.model.onFailure
import com.hfad.model.onSuccess

class OfferRepositoryImpl constructor(private val offerDataSource: OfferDataSource,
    private val remoteProviderDataSource: RemoteProviderDataSource,
    private val remoteUserRequestDataSource: RemoteUserRequestDataSource,
    private val remoteOfferDataSource: RemoteOfferDataSource
):OfferRepository {
    override suspend fun getAllOffers(requestSid: String): RepoResult<List<OfferResponseModel>> {
        try{
            val requestDetails = remoteUserRequestDataSource.getOne(requestSid)
           requestDetails.onSuccess {
               val allRequestOffers= mutableListOf<OfferResponseModel>()
               it.offers?.forEach {
                   Log.d("userOffer-GetAll",it.toString())
                   remoteOfferDataSource.getOne(it).onSuccess {
                       Log.d("userOffer-GetAll",it.toString())
                       allRequestOffers.add(it)
                   }


               }
               return Success(allRequestOffers)
           }.onFailure { return Failure(it) }
        }catch (e: Exception){
            e.printStackTrace()
            Failure(BasicError(Throwable("Getting All Data Failed")))
        }
        return Failure(BasicError(Throwable("Getting All Data Failed")))
    }

    override suspend fun getAllMyOffers(serviceProviderId: String): RepoResult<List<OfferResponseModel>> {
        try{
            val providerDetails = remoteProviderDataSource.getById(serviceProviderId)
            providerDetails.onSuccess {
                val allProviderOffers= mutableListOf<OfferResponseModel>()
                it.offers?.forEach {

                    remoteOfferDataSource.getOne(it.trim()).onSuccess {

                        allProviderOffers.add(it)
                    }


                }
                return Success(allProviderOffers)
            }.onFailure { return Failure(it) }
        }catch (e: Exception){
            e.printStackTrace()
            Failure(BasicError(Throwable("Getting All Data Failed")))
        }
        return Failure(BasicError(Throwable("Getting All Data Failed")))
    }

    override suspend fun getOffer(
        sid: String
    ): RepoResult<OfferResponseModel> {
        try {
            remoteOfferDataSource.getOne(sid)
                .onSuccess { return Success(it) }
                .onFailure { return Failure(it) }
        }catch (e: Exception){
            e.printStackTrace()
            Failure(BasicError(Throwable("Offer fetch failed")))
        }
        return Failure(BasicError(Throwable("Offer fetch failed")))
    }

    override suspend fun getOfferByUserRequestId(userRequestUIID: String): RepoResult<OfferResponseModel> {
        try {
            remoteOfferDataSource.getOfferByUserRequestID(userRequestUIID)
                .onSuccess { return Success(it) }
                .onFailure { return Failure(it) }
        }catch (e: Exception){
            e.printStackTrace()
            Failure(BasicError(Throwable("Offer fetch failed")))
        }
        return Failure(BasicError(Throwable("Offer fetch failed")))
    }

    override suspend fun hasOffer(sid: String): RepoResult<Int> {
        try{
       remoteUserRequestDataSource.getOne(sid)
           .onSuccess {
               if(it.offers!=null)
               return Success(it.offers!!.size) }
           .onFailure { return Failure(it) }
    }catch (e: Exception) {
            e.printStackTrace()
            Failure(BasicError(Throwable("Offer Exists fetch failed")))
        }
        return Failure(BasicError(Throwable("Offer Exists fetch failed")))
    }

    override suspend fun deleteOffer(
        sid: String
    ): RepoResult<Unit> {
        val offer = remoteOfferDataSource.getOne(sid)
        offer.onSuccess {
            remoteOfferDataSource.delete(sid)
            remoteUserRequestDataSource.deleteOffers(it.userRequestUUID,sid)
            remoteProviderDataSource.deleteOffers(it.serviceProviderId,sid)
            return Success(Unit)
        }.onFailure {
            Log.d("DELETE_OFFER",offer.toString())
            return Failure(BasicError(Throwable("Failed to delete offer")))
        }
        return Failure(BasicError(Throwable("Failed to delete offer")))
    }

    override suspend fun updateOffer(
        sid: String,
        offer: OfferRequestModel
    ): RepoResult<Unit> {
        return remoteOfferDataSource.update( sid, offer)
    }

    override suspend fun checkCreatedOffers(userRequestUIID: String, providerId: String): RepoResult<Boolean> {
        val requestResult = remoteUserRequestDataSource.getOne(userRequestUIID)
        val providerResult = remoteProviderDataSource.getById(providerId)

        return when {
            requestResult is Error -> Failure(BasicError(Throwable("Failed to get user request data")))
            providerResult is Error -> Failure(BasicError(Throwable("Failed to get provider data")))
            requestResult is Success && providerResult is Success -> {
                val userRequestOffers = requestResult.data.offers ?: emptyList()
                val providerOffers = providerResult.data.offers ?: emptyList()

                val commonElements = userRequestOffers.intersect(providerOffers.toSet())

                if (commonElements.isNotEmpty()) {
                    Success(true)
                } else {
                    Failure(BasicError(Throwable("There isnt any offers created")))
                }
            }
            else -> Failure(BasicError(Throwable("Error")))
        }
    }

    override suspend fun updateOfferStatus(
        sid: String,
        status: String
    ): RepoResult<Unit> {
        return remoteOfferDataSource.updateStatus( sid, status)
    }

    override suspend fun createOffer(offer: OfferRequestModel): RepoResult<Unit> {
        val newOffer = remoteOfferDataSource.create(offer)
        newOffer.onSuccess {
            Log.d("UUID",offer.userRequestUUID)
            val sid = it.sid

            remoteOfferDataSource.update(sid,offer.copy(sid=sid))
            remoteProviderDataSource.updateOffers(offer.serviceProviderId,it.sid)
                .onSuccess {
                    remoteUserRequestDataSource.updateOffers(offer.userRequestUUID.trim(),sid)
                }


            return Success(Unit)
        }.onFailure {
            return Failure(BasicError(Throwable("Failed to create offer")))
        }
        return Failure(BasicError(Throwable("Failed to create offer")))
    }
}