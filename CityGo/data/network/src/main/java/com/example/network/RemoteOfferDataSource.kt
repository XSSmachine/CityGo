package com.example.network


import android.util.Log
import com.example.network.entities.OfferRemoteEntity
import com.example.network.entities.toOfferRemoteEntity
import com.example.network.entities.toOfferResponseModel
import com.example.network.interfaces.OfferRemoteDataSource
import com.example.network.interfaces.dao.RemoteOfferDao
import com.hfad.model.BasicError
import com.hfad.model.ErrorCode
import com.hfad.model.Failure
import com.hfad.model.OfferRequestModel
import com.hfad.model.OfferResponseModel
import com.hfad.model.RepoResult
import com.hfad.model.ResponseBody
import com.hfad.model.Success
import retrofit2.http.Query


class RemoteOfferDataSource constructor(private val dao: RemoteOfferDao):
    OfferRemoteDataSource {
    override suspend fun getAll(): RepoResult<List<OfferResponseModel>> = try {
        val response = dao.getAll()
        if (response.isSuccessful) {
            val list = response.body()?.map { it.toOfferResponseModel() }
            if (!list.isNullOrEmpty()) {
                Success(list)
            } else {
                Failure(BasicError(Exception("Offers are empty")))
            }
        } else {
            Failure(BasicError(Exception("Failed to fetch offers: ${response.errorBody()?.string()}")))
        }
    } catch (e: Exception) {
        Failure(BasicError(e))
    }


//    override suspend fun getAllForCurrentUser(serviceProviderId: String): RepoResult<List<OfferResponseModel>> = try {
//        val list = dao.getAllForCurrentProvider(providerId = serviceProviderId).map { it.toOfferResponseModel() }
//        if (list!=null){
//            Success(list)
//        }else{
//            Failure(BasicError(Exception("Offers are empty")))
//        }
//    }catch (e:Exception){
//        Failure(BasicError(e))
//    }

    override suspend fun getOne(sid: String): RepoResult<OfferResponseModel> = try {
        val response = dao.getById(sid)
        if (response != null && response.isSuccessful) {
            val offerResponse = response.body()
            if (offerResponse != null) {
                val offer = offerResponse.toOfferResponseModel()
                if (offer != null) {
                    Log.d("userOffer-GetOne", offer.toString())
                    Success(offer)
                } else {
                    Log.d("userOffer-GetOne", "Offer conversion failed")
                    Failure(BasicError(Exception("Offer not found")))
                }
            } else {
                Log.d("userOffer-GetOne", "Response body is null")
                Failure(BasicError(Exception("Failed to fetch offer: Response body is null")))
            }
        } else {
            val errorMessage = response?.errorBody()?.string() ?: "Unknown error"
            Log.d("userOffer-GetOne", "Failed to fetch offer: $errorMessage")
            Failure(BasicError(Exception("Failed to fetch offer: $errorMessage")))
        }
    } catch (e: Exception) {
        Log.d("userOffer-GetOne", e.toString())
        Failure(BasicError(e))
    }

    suspend fun getOfferByUserRequestID(userRequestId: String): RepoResult<OfferResponseModel> = try {
        val response = dao.getOfferByUserRequestID(userRequestId)
        if (response != null && response.isSuccessful) {
            Log.d("userOffer-GetOneSucess", response.raw().toString())
            val offerResponse: Map<String, OfferRemoteEntity>? = response.body()
            if (offerResponse != null) {
                val offers = offerResponse.values.mapNotNull { it.toOfferResponseModel() }
                if (offers.isNotEmpty()) {
                    Log.d("userOffer-GetOne", offers.toString())
                    Success(offers.first()) // Assuming you want the first offer if multiple are returned
                } else {
                    Log.d("userOffer-GetOneSucess", response.raw().toString())
                    Log.d("userOffer-GetOne", "Offer conversion failed")
                    Failure(BasicError(Exception("Offer not found")))
                }
            } else {
                Log.d("userOffer-GetOne", "Response body is null")
                Failure(BasicError(Exception("Failed to fetch offer: Response body is null")))
            }
        } else {
            val errorMessage = response?.errorBody()?.string() ?: "Unknown error"
            Log.d("userOffer-GetOne", "Failed to fetch offer: $errorMessage")
            Failure(BasicError(Exception("Failed to fetch offer: $errorMessage")))
        }
    } catch (e: Exception) {
        Log.d("userOffer-GetOne", e.toString())
        Failure(BasicError(e))
    }




    override suspend fun delete(sid: String): RepoResult<Unit> {
        return try {
            dao.deleteById(sid)
            Success(Unit) // Return Success with Unit since there's no body in the response
        } catch (e: Exception) {
            Failure(BasicError(e, ErrorCode.ERROR))
        }
    }

    override suspend fun update(sid: String, offer: OfferRequestModel): RepoResult<Unit> {
        return try {
            dao.updateUserRequest(sid,offer.toOfferRemoteEntity())
            Success(Unit) // Return Success with Unit since there's no body in the response
        } catch (e: Exception) {
            Failure(BasicError(e, ErrorCode.ERROR))
        }
    }

    override suspend fun updateStatus(sid: String, status: Map<String,String>): RepoResult<Unit> {
        return try {
            dao.updateOfferStatus(sid.trim(),status)
            Success(Unit) // Return Success with Unit since there's no body in the response
        } catch (e: Exception) {
            Failure(BasicError(e, ErrorCode.ERROR))
        }
    }

    override suspend fun create(offer: OfferRequestModel): RepoResult<ResponseBody> {
        return try {
            val response = dao.addOffer(offer.toOfferRemoteEntity())
            if (response.isSuccessful) {
                Success(response.body()!!)
            } else {
                Failure(BasicError(Exception("API call failed with response: $response"), ErrorCode.ERROR))
            }
        } catch (e: Exception) {
            Failure(BasicError(e, ErrorCode.ERROR))
        }
    }


//    override suspend fun doesOfferExist(
//        userRequestId: String,
//        serviceProviderId: String
//    ): RepoResult<Boolean> = try{
//        val list = dao.checkIfOfferExists(requestId = userRequestId, providerId = serviceProviderId)
//        if (list.isEmpty()){
//            Success(false)
//        }else{
//            Success(true)
//        }
//    }catch (e:Exception){
//        Failure(BasicError(e))
//    }

}