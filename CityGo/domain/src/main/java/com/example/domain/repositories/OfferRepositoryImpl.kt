package com.example.domain.repositories

import com.example.domain.interfaces.OfferRepository
import com.example.repository.interfaces.OfferDataSource
import com.hfad.model.OfferRequestModel
import com.hfad.model.OfferResponseModel

class OfferRepositoryImpl constructor(private val offerDataSource: OfferDataSource
):OfferRepository {
    override suspend fun getAllOffers(userRequestId: Int): List<OfferResponseModel> {
        return offerDataSource.getAll(userRequestId)
    }

    override suspend fun getAllMyOffers(serviceProviderId: String): List<OfferResponseModel> {
        return offerDataSource.getAllForCurrentUser(serviceProviderId)
    }

    override suspend fun getOffer(
        userRequestId: Int,
        serviceProviderId: String
    ): OfferResponseModel? {
        return offerDataSource.getOne( userRequestId, serviceProviderId)
    }

    override suspend fun hasOffer(userRequestId: Int, serviceProviderId: String): Int {
        return offerDataSource.doesOfferExist(userRequestId,serviceProviderId)
    }

    override suspend fun deleteOffer(
        userRequestId: Int,
        serviceProviderId: String
    ) {
        offerDataSource.delete( userRequestId, serviceProviderId)
    }

    override suspend fun updateOffer(
        userRequestId: Int,
        serviceProviderId: String,
        offer: OfferRequestModel
    ) {
        offerDataSource.update( userRequestId, serviceProviderId, offer)
    }

    override suspend fun updateOfferStatus(
        userRequestId: Int,
        serviceProviderId: String,
        status: String
    ) {
        offerDataSource.updateStatus( userRequestId, serviceProviderId, status)
    }

    override suspend fun createOffer(offer: OfferRequestModel) {
        offerDataSource.create(offer)
    }
}