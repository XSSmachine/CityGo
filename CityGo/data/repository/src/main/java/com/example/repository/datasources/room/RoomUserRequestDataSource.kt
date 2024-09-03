package com.example.repository.datasources.room


import com.example.repository.datasources.room.entities.toUserRequestResponseModel
import com.example.repository.datasources.room.entities.toUserRequestRoomEntity
import com.example.repository.interfaces.UserRequestDao
import com.example.repository.interfaces.UserRequestDataSource
import com.hfad.model.BasicError
import com.hfad.model.ErrorCode
import com.hfad.model.Failure
import com.hfad.model.RepoResult
import com.hfad.model.Success
import com.hfad.model.UserRequestRequestModel
import com.hfad.model.UserRequestResponseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import java.util.UUID

/**
 * RoomUserRequestDataSource is an implementation of the UserRequestDataSource interface
 * that interacts with a Room database to perform CRUD operations on user request entities
 *
 * @property dao The Data Access Object (DAO) interface for user request entities
 * @author Karlo Kovačević
 */
class RoomUserRequestDataSource constructor(private val dao: UserRequestDao) :
    UserRequestDataSource {

    /**
     * Retrieves all user requests associated with the specified user ID from the database
     *
     * @param userId The ID of the user whose requests are to be retrieved
     * @return A list of UserRequestResponseModel objects representing user requests
     * @author Karlo Kovačević
     */
    override suspend fun getAll(): RepoResult<List<UserRequestResponseModel>> = try{
        val requests = dao.getAll().toList().map {
            it.toUserRequestResponseModel()
        }
        Success(requests)
    }catch (e: Exception){
        Failure(BasicError(e))
    }

    override suspend fun getAllForCurrentUser(userId: String): RepoResult<List<UserRequestResponseModel>> = try {
        val requests = dao.getAllForCurrentUser(userId).toList().map {
            it.toUserRequestResponseModel()
        }
        Success(requests)
    } catch (e: Exception){
            Failure(BasicError(e))
        }

    /**
     * Retrieves a single user request by its ID associated with the specified user ID from the database
     *
     * @param userId The ID of the user to whom the request belongs
     * @param id The ID of the user request to retrieve
     * @return A UserRequestResponseModel object representing the user request, or null if not found
     * @author Karlo Kovačević
     */
    override suspend fun getOne(
        userId: String,
        uuid: String
    ): RepoResult<UserRequestResponseModel> = try{
        val entity = dao.getByUserId(uuid,userId)
        Success(entity.toUserRequestResponseModel())
    }catch (e: Exception){
        Failure(BasicError(e))
    }

    override suspend fun getOneById(uuid: String): RepoResult<UserRequestResponseModel> = try{
        val entity = dao.getById(uuid)
        Success(entity.toUserRequestResponseModel())
    }catch (e: Exception){
        Failure(BasicError(e))
    }

    /**
     * Deletes a user request associated with the specified user ID from the database
     *
     * @param userId The ID of the user to whom the request belongs
     * @param id The ID of the user request to delete
     * @author Karlo Kovačević
     */
    override suspend fun delete(userId: String, uuid: String): RepoResult<Unit> {
        return try {
            dao.deleteById(uuid,userId)
            Success(Unit) // Return Success with Unit since there's no body in the response
        } catch (e: Exception) {
            Failure(BasicError(e, ErrorCode.ERROR))
        }
    }

    /**
     * Updates the description of a user request associated with the specified user ID in the database
     *
     * @param userId The ID of the user to whom the request belongs
     * @param id The ID of the user request to update
     * @param userRequest The updated UserRequestRequestModel object containing the new description
     * @author Karlo Kovačević
     */
    override suspend fun update(userId: String, uuid: String, userRequest: UserRequestRequestModel): RepoResult<Unit> {
        return try {
            dao.updateUserRequest(uuid,userId, userRequest.photo!!,userRequest.description,userRequest.address1.addressName,
                userRequest.address1.latitude,userRequest.address1.longitude,userRequest.address1.liftStairs,
                userRequest.address1.floor,userRequest.address1.doorCode,userRequest.address1.phoneNumber,
                userRequest.address2.addressName,userRequest.address2.latitude,userRequest.address2.longitude,
                userRequest.address2.liftStairs,userRequest.address2.floor,userRequest.address2.doorCode,
                userRequest.address2.phoneNumber,userRequest.timeTable,userRequest.category,userRequest.extraWorker,
                userRequest.price)
            Success(Unit) // Return Success with Unit since there's no body in the response
        } catch (e: Exception) {
            Failure(BasicError(e, ErrorCode.ERROR))
        }
    }

    /**
     * Creates a new user request associated with the specified user ID in the database
     *
     * @param userId The ID of the user to whom the request belongs
     * @param data The UserRequestRequestModel object containing the details of the new request
     * @author Karlo Kovačević
     */
    override suspend fun create(data: UserRequestRequestModel): RepoResult<Unit> {
        return try {
            val uuid = UUID.randomUUID().toString()
            val request = data.copy(uuid = uuid).toUserRequestRoomEntity()
            dao.insert(request)
            Success(Unit)
        } catch (throwable: Throwable) {
            Failure(BasicError(throwable))
        }
    }

}
