package com.example.domain.repositories

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

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
import com.hfad.model.Progress
import com.hfad.model.RepoResult
import com.hfad.model.ServiceProviderProfileResponseModel
import com.hfad.model.Success
import com.hfad.model.UserProfileRequestModel
import com.hfad.model.onFailure
import com.hfad.model.onSuccess
import com.hfad.model.toRequestModel
import kotlinx.coroutines.flow.Flow
import java.util.UUID


class UserRequestRepositoryImpl constructor(
    private val userRequestDataSource: UserRequestDataSource,
    private val remoteDataSource: UserRequestRemoteDataSource,
    private val remoteUserDataSource: RemoteUserDataSource,
    private val remoteImageDataSource: RemoteImageDataSource
) : UserRequestRepository {
    override suspend fun getAllUserRequests(): RepoResult<List<UserRequestResponseModel>> {
        try {
            remoteDataSource.getAll()
                .onSuccess { return Success(it) }
                .onFailure { return Failure(it) }
        } catch (e: Exception) {
            e.printStackTrace()
            Failure(BasicError(Throwable("Get All User Requests Fetch failed")))
        }
        return Failure(BasicError(Throwable("Get All User Requests Fetch failed")))

    }

    override suspend fun getAllUserRequestsForCurrentUser(userId: String): RepoResult<List<UserRequestResponseModel>> {
        return try {
            when (val remoteResult = remoteDataSource.getAllForCurrentUser(userId.trim())) {
                is Success -> {
                    val remoteList = remoteResult.data
                    Log.d("REMOTE LIST", remoteList.toString())
                    when (val localResult = userRequestDataSource.getAllForCurrentUser(userId)) {
                        is Success -> {
                            val localList = localResult.data
                            val missingEntries = remoteList.filterNot { remoteItem ->
                                localList.any { localItem -> localItem.sid == remoteItem.sid || localItem.address1 == remoteItem.address1 && localItem.address2 == remoteItem.address2 && localItem.description == remoteItem.description }
                            }
                            if (missingEntries.isNotEmpty()) {
                                missingEntries.forEach { element ->
                                    userRequestDataSource.create(element.toRequestModel())
                                }
                            }
                            Success(localList)
                        }

                        is Failure -> Failure(localResult.error)
                        is Progress -> TODO()
                    }
                }

                is Failure -> {
                    userRequestDataSource.getAllForCurrentUser(userId)
                }
                is Progress -> TODO()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Failure(BasicError(Throwable("Get All Current User Requests Fetch failed", e)))
        }
    }


    override suspend fun getUserRequest(
        userId: String,
        uuid: String
    ): RepoResult<UserRequestResponseModel> {
        try {
            userRequestDataSource.getOne(userId, uuid)
                .onSuccess { return Success(it) }
                .onFailure { return Failure(it) }
        } catch (e: Exception) {
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
                    Log.d("JUSTCHECKIN2", it.toString())
                    return Failure(it)
                }
        } catch (e: Exception) {
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
        } catch (e: Exception) {
            e.printStackTrace()
            Failure(BasicError(Throwable("Get User Request By Id Fetch failed")))
        }
        return Failure(BasicError(Throwable("Get User Request By Id Fetch failed")))

    }

    override suspend fun deleteUserRequest(userId: String, uuid: String): RepoResult<Unit> {
        return try {
            val updatedRoomEntity = userRequestDataSource.getOneById(uuid)
            updatedRoomEntity
                .onSuccess {
                    if (it != null && it.sid != null) {
                        val sid = it.sid
                        if (sid != null) {
                            val request = remoteDataSource.getOne(sid)
                            request.onSuccess { remoteImageDataSource.deleteImage(it.photo.toString()) }
                            remoteDataSource.delete(sid)
                            remoteUserDataSource.deleteUserRequests(userId, sid)
                        }
                    }
                }
            userRequestDataSource.delete(userId, uuid)
            Success(Unit)
        } catch (e: Exception) {
            // Handle any update failure
            Failure(BasicError(e))
        }
    }
    override suspend fun updateUserRequest(
        userId: String,
        uuid: String,
        data: UserRequestRequestModel
    ): RepoResult<Unit> {
        return try {
            // Get the current Room entity
            val currentRoomEntity = userRequestDataSource.getOneById(uuid)
            if (currentRoomEntity == null) {
                return Failure(BasicError(Throwable("User request not found in local database")))
            }
            // Update user request in Room
            val roomUpdateResult = userRequestDataSource.update(userId, uuid, data)

            roomUpdateResult.onSuccess {
                currentRoomEntity
                    .onSuccess {
                        if (it != null && it.sid != null) {
                            val sid = it.sid
                            // Update user request in Firebase using the sid from the updated Room entity
                            if (sid != null) {
                                if (it.photo.toString()==data.photo){
                                    remoteDataSource.update(
                                        sid,
                                        data.copy(sync = System.currentTimeMillis(),photo = null)
                                    )
                                }else{
                                    val imageUrl = remoteImageDataSource.uploadImage(data.photo!!)

                                    if (imageUrl == null) {
                                        Log.e("FIREBASE", "Failed to upload image")
                                        return Failure(BasicError(Throwable("Failed to upload image")))
                                    }
                                    remoteDataSource.update(
                                        sid,
                                        data.copy(sync = System.currentTimeMillis(), photo = imageUrl)
                                    )
                                }

                            }
                        }
                    }
            }.onFailure {

            }

            Success(Unit)
        } catch (e: Exception) {
            // Handle any update failure
            Failure(BasicError(e))
        }
    }


    override suspend fun createUserRequest(data: UserRequestRequestModel): RepoResult<Unit> {
        val imageUrl = remoteImageDataSource.uploadImage(data.photo!!)

        if (imageUrl == null) {
            Log.e("FIREBASE", "Failed to upload image")
            return Failure(BasicError(Throwable("Failed to upload image")))
        }
        val updatedDataWithImage = data.copy(photo = imageUrl)
        val result = remoteDataSource.create(updatedDataWithImage)
        if (result is Success) {
            val sid = result.data.sid
            try {
                val updatedData =
                    updatedDataWithImage.copy(sid = sid, sync = System.currentTimeMillis())
                remoteDataSource.update(sid, updatedData)
                remoteUserDataSource.updateUserRequests(data.userId, sid)
                userRequestDataSource.create(
                    data.copy(
                        sid = sid,
                        sync = System.currentTimeMillis()
                    )
                )
                return Success(Unit)
            } catch (e: Exception) {
                return Failure(BasicError(Throwable("Exception during Room DB operation")))
            }
        } else {
            return Failure(BasicError(Throwable("Exception during Realtime DB operation")))

        }
    }
}

