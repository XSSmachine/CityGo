package com.example.repository.datasources.room


import com.example.repository.datasources.room.entities.toUserProfileResponseModel
import com.example.repository.datasources.room.entities.toUserProfileRoomEntity
import com.example.repository.interfaces.UserDao
import com.example.repository.interfaces.UsersDataSource
import com.hfad.model.BasicError
import com.hfad.model.ErrorCode
import com.hfad.model.Failure
import com.hfad.model.RepoResult
import com.hfad.model.Success
import com.hfad.model.UserProfileRequestModel
import com.hfad.model.UserProfileResponseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Room implementation of the UsersDataSource interface
 * This class handles interactions with the local Room database for user-related operations
 *
 * @property dao The UserDao interface used to interact with the Room database
 * @author Karlo Kovačević
 */
class RoomUserDataSource constructor(private val dao: UserDao
) : UsersDataSource {

    /**
     * Creates a new user with the provided phone number, name, surname, and email
     *
     * @param phoneNumber The phone number of the user to be created
     * @param name The first name of the user
     * @param surname The last name of the user
     * @param email The email address of the user (optional)
     * @author Karlo Kovačević
     */
    override suspend fun createUserWithPhoneNumber(
        userId: String,
        phoneNumber: String,
        name: String,
        surname: String,
        email: String?,
        stars:Double,
        sid:String?
    ): RepoResult<Unit> {
        val user = UserProfileRequestModel(
            id = userId,
            name = name,
            surname = surname,
            email = email,
            phoneNumber = phoneNumber,
            profilePicture = "",
            stars = 3.50,
            sid = sid,
            sync = System.currentTimeMillis(),
            requests = emptyList()
        )
        return try {
            dao.createUser(user.toUserProfileRoomEntity())
            Success(Unit) // Return Success with Unit since there's no body in the response
        } catch (e: Exception) {
            Failure(BasicError(e, ErrorCode.ERROR))
        }
    }

    /**
     * Checks if a user with the given phone number exists in the database
     *
     * @param phoneNumber The phone number to check
     * @return True if the user exists, false otherwise
     * @author Karlo Kovačević
     */
    override suspend fun userExists(phoneNumber: String): RepoResult<Boolean> = try{
        val exists = dao.userExists(phoneNumber)
        Success(exists)
    }catch (e: Exception){
        Failure(BasicError(e))
    }

    /**
     * Retrieves a user's profile information by their phone number
     *
     * @param userId The phone number of the user to retrieve
     * @return The user's profile information if found, null otherwise
     * @author Karlo Kovačević
     */
    override suspend fun getUserById(userId: String): RepoResult<UserProfileResponseModel> = try {
        val user = dao.getById(userId).toUserProfileResponseModel()
        Success(user)
    }catch (e: Exception)
    {
        Failure(BasicError(e))
    }
    /**
     * Updates the profile information of a user
     *
     * @param userId The ID of the user to update
     * @param data The updated profile information of the user
     * @author Karlo Kovačević
     */
    override suspend fun updateUser(userId: String, data: UserProfileRequestModel): RepoResult<Unit> {
        return try {
            dao.updateUser(userId, data.name, data.surname, data.profilePicture ?: "",data.email ?: "",data.sync)
            Success(Unit) // Return Success with Unit since there's no body in the response
        } catch (e: Exception) {
            Failure(BasicError(e, ErrorCode.ERROR))
        }
    }

    /**
     * Deletes a user with the specified ID
     *
     * @param userId The ID of the user to delete
     * @author Karlo Kovačević
     */
    override suspend fun deleteUser(userId: String): RepoResult<Unit> {
        return try {
            dao.deleteUser(userId)
            Success(Unit) // Return Success with Unit since there's no body in the response
        } catch (e: Exception) {
            Failure(BasicError(e, ErrorCode.ERROR))
        }
    }
}
