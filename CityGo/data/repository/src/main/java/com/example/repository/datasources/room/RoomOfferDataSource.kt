package com.example.repository.datasources.room

import com.example.repository.datasources.room.entities.toOfferResponseModel
import com.example.repository.datasources.room.entities.toOfferRoomEntity
import com.example.repository.datasources.room.entities.toUserRequestResponseModel
import com.example.repository.datasources.room.entities.toUserRequestRoomEntity
import com.example.repository.interfaces.NullableOfferModel
import com.example.repository.interfaces.OfferDao
import com.example.repository.interfaces.OfferDataSource
import com.hfad.model.BasicError
import com.hfad.model.ErrorCode
import com.hfad.model.Failure
import com.hfad.model.OfferRequestModel
import com.hfad.model.OfferResponseModel
import com.hfad.model.RepoResult
import com.hfad.model.Success
import com.hfad.model.UserRequestRequestModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

class RoomOfferDataSource constructor(private val dao: OfferDao):OfferDataSource {
    override suspend fun getAll(requestUIID: String): RepoResult<List<OfferResponseModel>> = try {
        val result =  dao.getAllOffers(requestUIID).toList().map {
            it.toOfferResponseModel()
        }
        Success(result)
    }catch (e: Exception){
        Failure(BasicError(Throwable(e.localizedMessage as String)))
    }

    override suspend fun getAllForCurrentUser(userId: String): RepoResult<List<OfferResponseModel>> = try {
        val result =  dao.getAllMyOffers(userId).toList().map {
            it.toOfferResponseModel()
        }
        Success(result)
    }catch (e: Exception){
        Failure(BasicError(Throwable(e.localizedMessage as String)))
    }

    override suspend fun getOne(
        userRequestUIID: String,
        serviceProviderId: String
    ): RepoResult<OfferResponseModel> = try {
            val offer = dao.getSingleOffer(userRequestUIID,serviceProviderId).toOfferResponseModel()
            Success(offer)
        } catch (e: Exception) {
            Failure(BasicError(e))
        }

    override suspend fun delete(userRequestUIID: String, serviceProviderId: String): RepoResult<Unit> {
        return try {
            dao.deleteOffer( userRequestUIID, serviceProviderId)
            Success(Unit) // Return Success with Unit since there's no body in the response
        } catch (e: Exception) {
            Failure(BasicError(e, ErrorCode.ERROR))
        }
    }

    override suspend fun update(
        userRequestUIID: String,
        serviceProviderId: String,
        offer: OfferRequestModel
    ): RepoResult<Unit> {
        return try {
            dao.updateOffer(userRequestUIID,serviceProviderId,offer.price,offer.timeTable.toString(),offer.status)
            Success(Unit) // Return Success with Unit since there's no body in the response
        } catch (e: Exception) {
            Failure(BasicError(e, ErrorCode.ERROR))
        }
    }

    override suspend fun updateStatus(
        userRequestUIID: String,
        serviceProviderId: String,
        status: String
    ): RepoResult<Unit> {
        return try {
            dao.updateOfferStatus( userRequestUIID, serviceProviderId, status)
            Success(Unit) // Return Success with Unit since there's no body in the response
        } catch (e: Exception) {
            Failure(BasicError(e, ErrorCode.ERROR))
        }
    }

    override suspend fun create(offer: OfferRequestModel): RepoResult<Unit> {
        return try {
            dao.createOffer(offer.toOfferRoomEntity())
            Success(Unit) // Return Success with Unit since there's no body in the response
        } catch (e: Exception) {
            Failure(BasicError(e, ErrorCode.ERROR))
        }
    }

    override suspend fun doesOfferExist(
        userRequestUIID: String,
        serviceProviderId: String
    ): RepoResult<Int> = try{
        val num=dao.hasOffer(userRequestUIID,serviceProviderId)
        Success(num)
    } catch (e: Exception) {
        Failure(BasicError(e))
    }

}