package com.example.repository.datasources.room


import com.example.repository.datasources.room.entities.toUserRequestResponseModel
import com.example.repository.datasources.room.entities.toUserRequestRoomEntity
import com.example.repository.interfaces.UserRequestDao
import com.example.repository.interfaces.UserRequestDataSource
import com.hfad.model.UserRequestRequestModel
import com.hfad.model.UserRequestResponseModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList

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
    override suspend fun getAll(userId: Int): List<UserRequestResponseModel> {
        return dao.getAll().toList().map {
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
    override suspend fun getOne(userId: Int, id: Int): UserRequestResponseModel? {
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
    override suspend fun delete(userId: Int, id: Int) {
        dao.deleteById(id)
    }

    /**
     * Updates the description of a user request associated with the specified user ID in the database
     *
     * @param userId The ID of the user to whom the request belongs
     * @param id The ID of the user request to update
     * @param userRequest The updated UserRequestRequestModel object containing the new description
     * @author Karlo Kovačević
     */
    override suspend fun update(userId: Int, id: Int, userRequest: UserRequestRequestModel) {
        dao.updateDescription(id, userRequest.description)
    }

    /**
     * Creates a new user request associated with the specified user ID in the database
     *
     * @param userId The ID of the user to whom the request belongs
     * @param data The UserRequestRequestModel object containing the details of the new request
     * @author Karlo Kovačević
     */
    override suspend fun create(userId: Int, data: UserRequestRequestModel) {
        dao.insert(data.toUserRequestRoomEntity())
    }
}
