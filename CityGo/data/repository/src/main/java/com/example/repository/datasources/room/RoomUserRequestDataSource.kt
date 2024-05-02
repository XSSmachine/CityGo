package com.example.repository.datasources.room


import com.example.repository.datasources.room.entities.toUserRequestResponseModel
import com.example.repository.datasources.room.entities.toUserRequestRoomEntity
import com.example.repository.interfaces.UserRequestDao
import com.example.repository.interfaces.UserRequestDataSource
import com.hfad.model.UserRequestRequestModel
import com.hfad.model.UserRequestResponseModel

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
    override suspend fun getAll(userId: String): List<UserRequestResponseModel> {
        return dao.getAll(userId).toList().map {
            it.toUserRequestResponseModel()
        }
    }

    override suspend fun getAllForCurrentUser(userId: String): List<UserRequestResponseModel> {
        return dao.getAllForCurrentUser(userId).toList().map {
            it.toUserRequestResponseModel()
        }
    }

    /**
     * Retrieves a single user request by its ID associated with the specified user ID from the database
     *
     * @param userId The ID of the user to whom the request belongs
     * @param id The ID of the user request to retrieve
     * @return A UserRequestResponseModel object representing the user request, or null if not found
     * @author Karlo Kovačević
     */
    override suspend fun getOne(userId: String, id: Int): UserRequestResponseModel? {
        val entity = dao.getByUserId(id,userId)
        return entity?.toUserRequestResponseModel()
    }

    override suspend fun getOneById( id: Int): UserRequestResponseModel? {
        val entity = dao.getById(id)
        return entity?.toUserRequestResponseModel()
    }

    /**
     * Deletes a user request associated with the specified user ID from the database
     *
     * @param userId The ID of the user to whom the request belongs
     * @param id The ID of the user request to delete
     * @author Karlo Kovačević
     */
    override suspend fun delete(userId: String, id: Int) {
        dao.deleteById(id,userId)
    }

    /**
     * Updates the description of a user request associated with the specified user ID in the database
     *
     * @param userId The ID of the user to whom the request belongs
     * @param id The ID of the user request to update
     * @param userRequest The updated UserRequestRequestModel object containing the new description
     * @author Karlo Kovačević
     */
    override suspend fun update(userId: String, id: Int, userRequest: UserRequestRequestModel) {
        dao.updateUserRequest(id,userId, userRequest.photo,userRequest.description,userRequest.address1.addressName,
            userRequest.address1.latitude,userRequest.address1.longitude,userRequest.address1.liftStairs,
            userRequest.address1.floor,userRequest.address1.doorCode,userRequest.address1.phoneNumber,
            userRequest.address2.addressName,userRequest.address2.latitude,userRequest.address2.longitude,
            userRequest.address2.liftStairs,userRequest.address2.floor,userRequest.address2.doorCode,
            userRequest.address2.phoneNumber,userRequest.timeTable,userRequest.category,userRequest.extraWorker,
            userRequest.price)
    }

    /**
     * Creates a new user request associated with the specified user ID in the database
     *
     * @param userId The ID of the user to whom the request belongs
     * @param data The UserRequestRequestModel object containing the details of the new request
     * @author Karlo Kovačević
     */
    override suspend fun create(data: UserRequestRequestModel) {
        dao.insert(data.toUserRequestRoomEntity())
    }
}
