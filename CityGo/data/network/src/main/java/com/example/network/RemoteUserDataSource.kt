package com.example.network

import android.util.Log
import com.example.network.entities.UserRemoteEntity
import com.example.network.entities.UserRemoteResponseEntity
import com.example.network.entities.toUserProfileRemoteEntity
import com.example.network.entities.toUserProfileResponseModel
import com.example.network.interfaces.UserRemoteDataSource
import com.example.network.interfaces.dao.RemoteUserDao
import com.hfad.model.BasicError
import com.hfad.model.ErrorCode
import com.hfad.model.Failure
import com.hfad.model.RepoResult
import com.hfad.model.ResponseBody
import com.hfad.model.Success
import com.hfad.model.UserProfileRequestModel
import com.hfad.model.UserProfileResponseModel

class RemoteUserDataSource constructor(private val dao: RemoteUserDao):UserRemoteDataSource {

    override suspend fun createUser(userId: String, data: UserProfileRequestModel): RepoResult<UserProfileResponseModel> {
        return try {
            val response = dao.addUser(userId,data.toUserProfileRemoteEntity())
            val userData = response.body()?.toUserProfileResponseModel()
            if (response.isSuccessful) {
                Log.d("REALTIME", response.raw().body().toString())
                if (userData != null) {
                    Success(userData)
                } else {
                    Failure(BasicError(Exception("User data is null"), ErrorCode.ERROR))
                }

            } else {
                Failure(BasicError(Exception("API call failed with response: $response"), ErrorCode.ERROR))
            }
        } catch (e: Exception) {
            Log.d("CREATE USER", "ERROR")
            Failure(BasicError(e, ErrorCode.ERROR))

        }
    }


    override suspend fun userExists(phoneNumber: String): RepoResult<Boolean> = try {
        val response = dao.checkUserExists(phoneNumber)
        if (response.isSuccessful) {
            Log.d("REALTIME", response.body().toString())
            val userMap = response.body()
            if (userMap != null && userMap.isNotEmpty()) {
                // Assuming the map has only one entry for a unique phone number
                val userEntry = userMap.entries.first()
                val user = userEntry.value.toUserProfileResponseModel()  // Adjust this line as needed
                Log.d("REALTIME", user.id?: "No ID")
                Success(true)
            } else {
                Failure(BasicError(Exception("User not found for phone number: $phoneNumber")))
            }
        } else {
            Log.d("REALTIME", response.raw().toString())
            Failure(BasicError(Exception("Failed to fetch user: ${response.errorBody()?.string()}")))
        }
    } catch (e: Exception) {
        Failure(BasicError(e))
    }

    // Extension function to map the response to UserProfileResponseModel
    fun Map.Entry<String, UserRemoteResponseEntity>.toUserProfileResponseModel(): UserRemoteResponseEntity {
        return this.value
    }



    override suspend fun getUserById(userId: String): RepoResult<UserProfileResponseModel> = try{
        val response = dao.getSingleUserById(userId)
        if (response.isSuccessful){
            Log.d("REALPROFIL", response.body().toString())
            val user = response.body()?.toUserProfileResponseModel()
            if (user != null) {
                Success(user)
            } else {
                Failure(BasicError(Exception("User data is null"), ErrorCode.ERROR))
            }
        }else{
            Failure(BasicError(Exception("User not found for ID: $userId")))
        }

    }catch (e:Exception){
        Failure(BasicError(e))
    }

    override suspend fun updateUser(userId: String, data: UserProfileRequestModel): RepoResult<Unit> {
        return try {
            val response = dao.updateUser(userId,data.toUserProfileRemoteEntity())
            if (response.isSuccessful) {
                Success(response.body()!!)
            } else {
                Failure(BasicError(Exception("API call failed with response: $response"), ErrorCode.ERROR))
            }
        } catch (e: Exception) {
            Failure(BasicError(e, ErrorCode.ERROR))
        }
    }

    override suspend fun updateUserRequests(userId: String, requestId: String): RepoResult<Unit> {
        return try {
            val user = dao.getSingleUserById(userId)
            if(user.isSuccessful){
                Log.d("USER_REQUEST", user.body().toString())
                val requests = user.body()?.requests?.toMutableList()?: mutableListOf()
                if (requests != null) {
                    requests.add(requestId)
                }

                    val requestsMap = mapOf("Requests" to (requests.toList() ))
                    val response =  dao.updateUserRequests(userId, requestsMap)


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

    override suspend fun  deleteUserRequests(userId:String, requestId:String):RepoResult<Unit>{
        return try {
            val user = dao.getSingleUserById(userId)
            if(user.isSuccessful){
                Log.d("USER_REQUEST", user.body().toString())
                val requests = user.body()?.requests?.toMutableList()?: mutableListOf()
                if (requests != null) {
                    requests.remove(requestId)
                }

                val requestsMap = mapOf("Requests" to (requests.toList() ))
                val response =  dao.updateUserRequests(userId, requestsMap)


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

    override suspend fun deleteUser(userId: String): RepoResult<Unit> {

        return try {
            val response = dao.deleteUser(userId)
            // delete all corresponding requests
            if (response.isSuccessful) {
                Success(response.body()!!)
            } else {
                Failure(BasicError(Exception("API call failed with response: $response"), ErrorCode.ERROR))
            }
        } catch (e: Exception) {
            Failure(BasicError(e, ErrorCode.ERROR))
        }
    }

}