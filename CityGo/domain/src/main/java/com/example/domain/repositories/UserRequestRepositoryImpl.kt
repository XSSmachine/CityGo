package com.example.domain.repositories

import android.util.Log

import com.example.domain.interfaces.UserRequestRepository
import com.example.network.RemoteImageDataSource
import com.example.network.RemoteUserDataSource
import com.example.network.entities.toUserRequestRemoteEntity
import com.example.network.interfaces.UserRequestRemoteDataSource
import com.example.repository.datasources.room.entities.toUserProfileRequestModel
import com.hfad.model.UserRequestRequestModel
import com.hfad.model.UserRequestResponseModel
import com.example.repository.interfaces.UserRequestDataSource
import com.hfad.model.BasicError
import com.hfad.model.Failure
import com.hfad.model.RepoResult
import com.hfad.model.ServiceProviderProfileResponseModel
import com.hfad.model.Success
import com.hfad.model.UserProfileRequestModel
import com.hfad.model.onFailure
import com.hfad.model.onSuccess
import kotlinx.coroutines.flow.Flow
import java.util.UUID


class UserRequestRepositoryImpl constructor(private val userRequestDataSource: UserRequestDataSource,private val remoteDataSource: UserRequestRemoteDataSource,private val remoteUserDataSource: RemoteUserDataSource, private val remoteImageDataSource: RemoteImageDataSource) : UserRequestRepository {
    override suspend fun getAllUserRequests(): RepoResult<List<UserRequestResponseModel>> {
        try {
            remoteDataSource.getAll()
                .onSuccess { return Success(it) }
                .onFailure { return Failure(it) }
        }catch (e:Exception){
            e.printStackTrace()
            Failure(BasicError(Throwable("Get All User Requests Fetch failed")))
        }
        return Failure(BasicError(Throwable("Get All User Requests Fetch failed")))

    }

    override suspend fun getAllUserRequestsForCurrentUser(userId:String): RepoResult<List<UserRequestResponseModel>> {
        try {
            userRequestDataSource.getAllForCurrentUser(userId)
                .onSuccess { return Success(it) }
                .onFailure { return Failure(it) }
        }catch (e:Exception){
            e.printStackTrace()
            Failure(BasicError(Throwable("Get All Current User Requests Fetch failed")))
        }
        return Failure(BasicError(Throwable("Get All Current User Requests Fetch failed")))
    }

    override suspend fun getUserRequest(userId:String, uuid: String): RepoResult<UserRequestResponseModel> {
        try {
            userRequestDataSource.getOne(userId,uuid)
                .onSuccess { return Success(it) }
                .onFailure { return Failure(it) }
        }catch (e:Exception){
            e.printStackTrace()
            Failure(BasicError(Throwable("Get User Request Fetch failed")))
        }
        return Failure(BasicError(Throwable("Get User Request Fetch failed")))
    }

    override suspend fun getRemoteUserRequest(sid: String): RepoResult<UserRequestResponseModel> {
        try {
            remoteDataSource.getOne(sid)
                .onSuccess { return Success(it) }
                .onFailure {
                    Log.d("JUSTCHECKIN2",it.toString())
                    return Failure(it) }
        }catch (e:Exception){
            e.printStackTrace()
            Failure(BasicError(Throwable("Get Remote User Request Fetch failed")))
        }
        return Failure(BasicError(Throwable("Get Remote User Request Fetch failed")))
    }

    override suspend fun getUserRequestById(uuid: String): RepoResult<UserRequestResponseModel> {
        try {
            userRequestDataSource.getOneById(uuid)
                .onSuccess { return Success(it) }
                .onFailure { return Failure(it) }
        }catch (e:Exception){
            e.printStackTrace()
            Failure(BasicError(Throwable("Get User Request By Id Fetch failed")))
        }
        return Failure(BasicError(Throwable("Get User Request By Id Fetch failed")))

    }

    override suspend fun deleteUserRequest(userId:String,uuid: String): RepoResult<Unit> {
//        return userRequestDataSource.delete(userId,uuid)

        return try {
            // Update user request in Room


            // Get the updated Room entity
            val updatedRoomEntity = userRequestDataSource.getOneById(uuid)


            updatedRoomEntity
                .onSuccess { if (it != null && it.sid!= null) {
                    val sid = it.sid
                    // Update user request in Firebase using the sid from the updated Room entity
                    if (sid != null) {
                        val request = remoteDataSource.getOne(sid)
                        request.onSuccess { remoteImageDataSource.deleteImage(it.photo.toString()) }
                        remoteDataSource.delete(sid)
                        remoteUserDataSource.deleteUserRequests(userId, sid)
                    }
                } }
            userRequestDataSource.delete(userId, uuid)


            // Check if the updated Room entity is not null and has a sid
            // Return success if both updates are successful
            Success(Unit)
        } catch (e: Exception) {
            // Handle any update failure
            Failure(BasicError(e))
        }
    }

//    override suspend fun updateUserRequest(userId:String,uuid: String, data: UserRequestRequestModel): RepoResult<Unit> {
//        return userRequestDataSource.update(userId,uuid, data)
//    }
    override suspend fun updateUserRequest(userId: String, uuid: String, data: UserRequestRequestModel): RepoResult<Unit> {
        return try {
            // Update user request in Room
            val roomUpdateResult = userRequestDataSource.update(userId, uuid, data)

            // Get the updated Room entity
            val updatedRoomEntity = userRequestDataSource.getOneById(uuid)

            updatedRoomEntity
                .onSuccess { if (it != null && it.sid!= null) {
                    val sid = it.sid
                    // Update user request in Firebase using the sid from the updated Room entity
                    if (sid != null) {

                        remoteDataSource.update(sid, data.copy(sync = System.currentTimeMillis()))
                    }
                } }

            // Check if the updated Room entity is not null and has a sid
            // Return success if both updates are successful
            Success(Unit)
        } catch (e: Exception) {
            // Handle any update failure
            Failure(BasicError(e))
        }
    }


    override suspend fun createUserRequest(data: UserRequestRequestModel):RepoResult<Unit> {
//        return userRequestDataSource.create(data)
        val imageUrl = remoteImageDataSource.uploadImage(data.photo)

        if (imageUrl == null) {
            Log.e("FIREBASE", "Failed to upload image")
            return Failure(BasicError(Throwable("Failed to upload image")))
        }
        val updatedDataWithImage = data.copy(photo = imageUrl)
        // Create a new user profile with the provided data using Firebase Realtime Database
        val result = remoteDataSource.create(updatedDataWithImage)

        // Check if the user was created successfully in Firebase
        if (result is Success) {
            val sid = result.data.sid
             Log.d("ROOM",sid)
            try {
                val updatedData = updatedDataWithImage.copy(sid=sid, sync = System.currentTimeMillis())
                Log.d("UPDATE REMOTE", updatedData.toString())
                remoteDataSource.update(sid,updatedData)
                // Save the user details in Room
                userRequestDataSource.create(data.copy(sid=sid, sync = System.currentTimeMillis()))
                Log.d("ROOMDB", "User saved in Room DB")

                remoteUserDataSource.updateUserRequests(data.userId,sid)

                    // Update the user entity with the updated list of requests


                return Success(Unit)
            } catch (e: Exception) {
                Log.e("ROOMDB", "Exception during Room DB operation", e)
                return Failure(BasicError(Throwable("Exception during Room DB operation")))
            }
        } else {
            Log.e("FIREBASE", "Failed to create user in Firebase")
            return Failure(BasicError(Throwable("Exception during Realtime DB operation")))

        }
    }
}