package com.example.network

import android.util.Log
import com.example.network.entities.UserRequestRemoteEntity
import com.example.network.entities.toUserProfileRemoteEntity
import com.example.network.entities.toUserRequestRemoteEntity
import com.example.network.entities.toUserRequestResponseModel
import com.example.network.interfaces.UserRemoteDataSource
import com.example.network.interfaces.UserRequestRemoteDataSource
import com.example.network.interfaces.dao.RemoteUserDao
import com.example.network.interfaces.dao.RemoteUserRequestDao
import com.hfad.model.BasicError
import com.hfad.model.Error
import com.hfad.model.ErrorCode
import com.hfad.model.Failure
import com.hfad.model.RepoResult
import com.hfad.model.ResponseBody
import com.hfad.model.Success
import com.hfad.model.UserProfileRequestModel
import com.hfad.model.UserProfileResponseModel
import com.hfad.model.UserRequestRequestModel
import com.hfad.model.UserRequestResponseModel
import java.util.UUID

class RemoteUserRequestDataSource constructor(private val dao: RemoteUserRequestDao):
    UserRequestRemoteDataSource {
    override suspend fun getOne(sid: String): RepoResult<UserRequestResponseModel> = try {
        val response = dao.getById(sid.trimEnd())
        Log.d("JUSTCHECKIN1",response.raw().toString())
        if (response.isSuccessful) {
            Log.d("JUSTCHECKIN",response.body().toString())
            val request = response.body()?.toUserRequestResponseModel()
            if (request != null) {
                Log.d("JUSTCHECKIN",request.toString())
                Success(request)
            } else {
                Failure(BasicError(Exception("User request not found for ID: $sid")))
            }
        } else {
            Failure(BasicError(Exception("Failed to fetch user request: ${response.errorBody()?.string()}")))
        }
    } catch (e: Exception) {
        Failure(BasicError(e))
    }

    override suspend fun getAll(): RepoResult<List<UserRequestResponseModel>> = try {
        // Fetch all user requests from the DAO
        val response = dao.getAll()

        if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                Log.d("RESPONSEBODY",responseBody.values.toString())
                val list = responseBody.values.map { it.toUserRequestResponseModel() }.reversed()
                if (list.isNotEmpty()) {
                    Log.d("GETALL", "SUCCESS: ${responseBody.toString()}")
                    Success(list)
                } else {
                    Log.d("GETALL", "FAIL: User requests are empty")
                    Failure(BasicError(Exception("User requests are empty")))
                }
            } else {
                Log.d("GETALL", "FAIL: Response body is null")
                Failure(BasicError(Exception("Response body is null")))
            }
        } else {
            val errorBody = response.errorBody()?.string()
            Log.d("GETALL", "FAIL: Failed to fetch user requests: $errorBody")
            Failure(BasicError(Exception("Failed to fetch user requests: $errorBody")))
        }
    } catch (e: Exception) {
        Log.d("GETALL", "FAIL: Exception occurred", e)
        Failure(BasicError(e))
    }





    override suspend fun getAllForCurrentUser(userID: String): RepoResult<List<UserRequestResponseModel>> = try{
        val response = dao.getAllForCurrentUser(userId = "\"$userID\"")

        if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                Log.d("RESPONSEBODY",responseBody.values.toString())
                val list = responseBody.values.map { it.toUserRequestResponseModel() }.reversed()
                if (list.isNotEmpty()) {
                    Log.d("GETALLMY", "SUCCESS: ${responseBody.toString()}")
                    Success(list)
                } else {
                    Log.d("GETALLMY", "FAIL: User requests are empty")
                    Failure(BasicError(Exception("User requests are empty")))
                }
            } else {
                Log.d("GETALLMY", "FAIL: Response body is null")
                Failure(BasicError(Exception("Response body is null")))
            }
        } else {
            val errorBody = response.errorBody()?.string()
            Log.d("GETALLMY", "FAIL: Failed to fetch user requests: $errorBody")
            Failure(BasicError(Exception("Failed to fetch user requests: $errorBody")))
        }
    }catch (e:Exception){
        Failure(BasicError(e))
    }


    override suspend fun delete(sid: String): RepoResult<Unit> {
        return try {
            val response = dao.deleteById(sid)
            if (response.isSuccessful) {
                Success(response.body()!!)
            } else {
                Failure(BasicError(Exception("API call failed with response: $response"), ErrorCode.ERROR))
            }
        } catch (e: Exception) {
            Failure(BasicError(e, ErrorCode.ERROR))
        }
    }

    override suspend fun update(sid: String, data: UserRequestRequestModel): RepoResult<Unit> {
        return try {
            val response = dao.updateUserRequest(sid,data.toUserRequestRemoteEntity())
            if (response.isSuccessful) {
                Success(response.body()!!)
            } else {
                Failure(BasicError(Exception("API call failed with response: $response"), ErrorCode.ERROR))
            }
        } catch (e: Exception) {
            Failure(BasicError(e, ErrorCode.ERROR))
        }
    }

    override suspend fun create(data: UserRequestRequestModel): RepoResult<ResponseBody> {
        return try {
            val uuid = UUID.randomUUID().toString()
            val requestEntity = data.copy(uuid=uuid).toUserRequestRemoteEntity()
            val response = dao.addUserRequest(requestEntity)
            if (response.isSuccessful) {
                Log.d("UNIZD",response.body().toString())
                Success(response.body()!!)
            } else {
                Log.d("UNIZD",response.body().toString())
                Failure(BasicError(Exception("API call failed with response: $response"), ErrorCode.ERROR))
            }
        } catch (e: Exception) {
            Log.d("UNIZD",e.toString())
            Failure(BasicError(e, ErrorCode.ERROR))
        }
    }

    override suspend fun updateOffers(sid: String, offerId: String): RepoResult<Unit> {
        return try {
            val user = dao.getById(sid.trimEnd())
            if(user.isSuccessful){
                Log.d("USER_REQUEST", user.raw().toString())
                Log.d("USER_REQUEST22", user.body().toString())
                val requests = user.body()?.offers?.toMutableList()?: mutableListOf()
                if (requests != null) {
                    Log.d("USER_REQUEST33", requests.toString())
                    requests.add(offerId)
                }

                val requestsMap = mapOf("Offers" to (requests.toList() ))
                Log.d("USER_REQUEST44", requestsMap.toString())
                val response =  dao.updateOffers(sid, requestsMap)


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

    override suspend fun  deleteOffers(sid: String, offerId:String):RepoResult<Unit>{
        return try {
            val user = dao.getById(sid.trim())
            if(user.isSuccessful){
                Log.d("USER_REQUEST", user.body().toString())
                val requests = user.body()?.offers?.toMutableList()?: mutableListOf()
                if (requests != null) {
                    requests.remove(offerId)
                }

                val requestsMap = mapOf("Offers" to (requests.toList() ))
                val response =  dao.updateOffers(sid.trim(), requestsMap)


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