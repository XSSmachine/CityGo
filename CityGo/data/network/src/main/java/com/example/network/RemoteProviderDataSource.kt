package com.example.network

import android.util.Log
import com.example.network.entities.toServiceProviderProfileResponseModel
import com.example.network.entities.toServiceProviderProfileRoomEntity
import com.example.network.interfaces.ProviderRemoteDataSource

import com.example.network.interfaces.dao.RemoteProviderDao
import com.hfad.model.BasicError
import com.hfad.model.ErrorCode
import com.hfad.model.Failure
import com.hfad.model.RepoResult
import com.hfad.model.ResponseBody
import com.hfad.model.ServiceProviderProfileRequestModel
import com.hfad.model.ServiceProviderProfileResponseModel
import com.hfad.model.Success


class RemoteProviderDataSource constructor(private val dao: RemoteProviderDao):
    ProviderRemoteDataSource {
    override suspend fun create(
        providerId: String,
        worker: ServiceProviderProfileRequestModel
    ): RepoResult<ServiceProviderProfileResponseModel> {
        return try {
            val response = dao.addProvider(providerId, worker.toServiceProviderProfileRoomEntity())
            if (response.isSuccessful) {
                val providerResponseModel = response.body()?.toServiceProviderProfileResponseModel()
                if (providerResponseModel != null) {
                    Success(providerResponseModel)
                } else {
                    Failure(
                        BasicError(
                            Exception("Failed to convert provider to response model"),
                            ErrorCode.ERROR
                        )
                    )
                }
            } else {
                Failure(
                    BasicError(
                        Exception("API call failed with response: $response"),
                        ErrorCode.ERROR
                    )
                )
            }
        } catch (e: Exception) {
            Failure(BasicError(e, ErrorCode.ERROR))
        }
    }


    override suspend fun update(
        id: String,
        worker: ServiceProviderProfileRequestModel
    ): RepoResult<Unit> {
        return try {
            val response = dao.updateProvider(id, worker.toServiceProviderProfileRoomEntity())
            if (response.isSuccessful) {
                Success(response.body()!!)
            } else {
                Failure(
                    BasicError(
                        Exception("API call failed with response: $response"),
                        ErrorCode.ERROR
                    )
                )
            }
        } catch (e: Exception) {
            Failure(BasicError(e, ErrorCode.ERROR))
        }
    }

    override suspend fun updateStatus(id: String, status: Map<String,String>): RepoResult<Unit>{  return try
    {
        val response = dao.updateProviderStatus(id, status)
        if (response.isSuccessful) {
            Success(response.body()!!)
        } else {
            Failure(
                BasicError(
                    Exception("API call failed with response: $response"),
                    ErrorCode.ERROR
                )
            )
        }
    } catch (e: Exception)
    {
        Failure(BasicError(e, ErrorCode.ERROR))
    }
}


    override suspend fun delete(cygoId: String): RepoResult<Unit> {

        return try {
            val response = dao.deleteProvider(id=cygoId)
            if (response.isSuccessful) {
                Success(response.body()!!)
            } else {
                Failure(BasicError(Exception("API call failed with response: $response"), ErrorCode.ERROR))
            }
        } catch (e: Exception) {
            Failure(BasicError(e, ErrorCode.ERROR))
        }
    }

    override suspend fun getById(cygoId: String): RepoResult<ServiceProviderProfileResponseModel> = try {
        val response = dao.getSingleProviderById(cygoId)
        if (response.isSuccessful) {
            val provider = response.body()?.toServiceProviderProfileResponseModel()
            if (provider != null) {
                Success(provider)
            } else {
                Failure(BasicError(Exception("Provider not found")))
            }
        } else {
            Failure(BasicError(Exception("Failed to fetch provider: ${response.errorBody()?.string()}")))
        }
    } catch (e: Exception) {
        Failure(BasicError(e))
    }

    override suspend fun getAll(): RepoResult<List<ServiceProviderProfileResponseModel>> = try {
        val response = dao.getAllProviders()
        if (response.isSuccessful) {
            val list = response.body()?.map { it.toServiceProviderProfileResponseModel() }
            if (!list.isNullOrEmpty()) {
                Success(list)
            } else {
                Failure(BasicError(Exception("Providers are empty")))
            }
        } else {
            Failure(BasicError(Exception("Failed to fetch providers: ${response.errorBody()?.string()}")))
        }
    } catch (e: Exception) {
        Failure(BasicError(e))
    }

    override suspend fun updateOffers(userId: String, offerId: String): RepoResult<Unit> {
        return try {
            val user = dao.getSingleProviderById(userId)
            if(user.isSuccessful){
                Log.d("USER_REQUEST", user.body().toString())
                val requests = user.body()?.offers?.toMutableList()?: mutableListOf()
                if (requests != null) {
                    requests.add(offerId)
                }

                val requestsMap = mapOf("Offers" to (requests.toList() ))
                val response =  dao.updateOffers(userId, requestsMap)


                if (response.isSuccessful) {
                    Success(response.body()!!)
                } else {
                    Log.d("USER_REQUEST", "ERROR1")
                    Failure(BasicError(Exception("API call failed with response: $response"), ErrorCode.ERROR))
                }
            }else {
                Log.d("USER_REQUEST", "ERROR")
                Failure(BasicError(Exception("API call failed with response: $user"), ErrorCode.ERROR))
            }
        }catch (e: Exception) {
            Log.d("USER_REQUEST", e.toString())
            Failure(BasicError(e, ErrorCode.ERROR))
        }

    }

    override suspend fun  deleteOffers(userId:String, offerId:String):RepoResult<Unit>{
        return try {
            val user = dao.getSingleProviderById(userId.trim())
            if(user.isSuccessful){
                Log.d("USER_REQUEST", user.body().toString())
                val requests = user.body()?.offers?.toMutableList()?: mutableListOf()
                if (requests != null) {
                    requests.remove(offerId)
                }

                val requestsMap = mapOf("Offers" to (requests.toList() ))
                val response =  dao.updateOffers(userId, requestsMap)


                if (response.isSuccessful) {
                    Success(response.body()!!)
                } else {
                    Log.d("USER_REQUEST", "ERROR1")
                    Failure(BasicError(Exception("API call failed with response: $response"), ErrorCode.ERROR))
                }
            }else {
                Log.d("USER_REQUEST", "ERROR")
                Failure(BasicError(Exception("API call failed with response: $user"), ErrorCode.ERROR))
            }
        }catch (e: Exception) {
            Log.d("USER_REQUEST", e.toString())
            Failure(BasicError(e, ErrorCode.ERROR))
        }
    }


}