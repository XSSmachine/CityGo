package com.example.repository.datasources.room

import com.example.repository.datasources.room.entities.toOfferResponseModel
import com.example.repository.datasources.room.entities.toOfferRoomEntity
import com.example.repository.datasources.room.entities.toUserRequestResponseModel
import com.example.repository.datasources.room.entities.toUserRequestRoomEntity
import com.example.repository.interfaces.OfferDao
import com.example.repository.interfaces.OfferDataSource
import com.hfad.model.OfferRequestModel
import com.hfad.model.OfferResponseModel
import com.hfad.model.UserRequestRequestModel

class RoomOfferDataSource constructor(private val dao: OfferDao):OfferDataSource {
    override suspend fun getAll(userRequestId: Int): List<OfferResponseModel> {
        return dao.getAllOffers(userRequestId).toList().map {
            it.toOfferResponseModel()
        }
    }

    override suspend fun getAllForCurrentUser(userId: String): List<OfferResponseModel> {
        return dao.getAllMyOffers(userId).toList().map {
            it.toOfferResponseModel()
        }
    }

    override suspend fun getOne(
        userRequestId: Int,
        serviceProviderId: String
    ): OfferResponseModel? {
        val offer = dao.getSingleOffer(userRequestId,serviceProviderId)
       return offer?.toOfferResponseModel()
    }

    override suspend fun delete( userRequestId: Int, serviceProviderId: String) {
        dao.deleteOffer( userRequestId, serviceProviderId)
    }

    override suspend fun update(
        userRequestId: Int,
        serviceProviderId: String,
        offer: OfferRequestModel
    ) {
        dao.updateOffer(userRequestId,serviceProviderId,offer.price,offer.timeTable.toString(),offer.status)
    }

    override suspend fun updateStatus(
        userRequestId: Int,
        serviceProviderId: String,
        status: String
    ) {
        dao.updateOfferStatus( userRequestId, serviceProviderId, status)
    }

    override suspend fun create(offer: OfferRequestModel) {
        dao.createOffer(offer.toOfferRoomEntity())
    }

    override suspend fun doesOfferExist(userRequestId: Int, serviceProviderId: String): Int {
        return dao.hasOffer(userRequestId,serviceProviderId)
    }
}