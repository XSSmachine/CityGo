package com.example.repository.interfaces

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import com.example.repository.datasources.room.entities.UserProfileRoomEntity
import com.example.repository.datasources.room.entities.UserRequestRoomEntity

/**
 * Room database class for the CityGo application.
 * Manages the entities and provides access to DAO interfaces.
 *
 * @author Karlo Kovačević
 */
@Database(entities = [UserRequestRoomEntity::class, UserProfileRoomEntity::class], version = 2)
abstract class CityGoDatabase : RoomDatabase() {
    /**
     * Provides access to the DAO interface for UserRequestRoomEntity.
     */
    abstract val contactDao: UserRequestDao
    /**
     * Provides access to the DAO interface for UserProfileRoomEntity.
     */
    abstract val userDao: UserDao

    companion object {
        const val DATABASE_NAME = "citygo_db"
    }
}

/**
 * Data Access Object (DAO) interface for accessing and managing UserRequestRoomEntity entities.
 */
@Dao
interface UserRequestDao {
    /**
     * Retrieves all user requests from the database.
     */
    @Query("SELECT * FROM user_requests")
    suspend fun getAll(): List<UserRequestRoomEntity>

    /**
     * Retrieves a user request by its ID from the database.
     */
    @Query("SELECT * FROM user_requests WHERE id = :id")
    suspend fun getById(id: Int): UserRequestRoomEntity?

    /**
     * Deletes a user request by its ID from the database.
     */
    @Query("DELETE FROM user_requests WHERE id = :id")
    suspend fun deleteById(id: Int)

    /**
     * Updates the description of a user request.
     */
    @Query("UPDATE user_requests SET description = :description WHERE id = :id")
    suspend fun updateDescription(id: Int, description: String)

    /**
     * Inserts a new user request into the database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact: UserRequestRoomEntity)
}

/**
 * Data Access Object (DAO) interface for accessing and managing UserProfileRoomEntity entities.
 */
@Dao
interface UserDao {
    /**
     * Inserts or updates a user profile into the database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createUser(user: UserProfileRoomEntity)

    /**
     * Updates the profile information of a user in the database.
     */
    @Query("UPDATE users SET name = :name, surname = :surname, profilePicture= :profilePic WHERE id = :id")
    suspend fun updateUser(id: Int, name: String, surname: String, profilePic: String)

    /**
     * Deletes a user by its ID from the database.
     */
    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUser(userId: Int)

    /**
     * Retrieves a user profile by their phone number from the database.
     */
    @Query("SELECT * FROM users WHERE phoneNumber = :phoneNum")
    suspend fun getByPhoneNumber(phoneNum: String): UserProfileRoomEntity?

    /**
     * Checks if a user with the given phone number exists in the database.
     */
    @Query("SELECT COUNT(*) FROM users WHERE phoneNumber = :phoneNum")
    suspend fun userExists(phoneNum: String): Boolean
}

