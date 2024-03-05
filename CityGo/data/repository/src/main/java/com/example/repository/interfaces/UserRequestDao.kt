package com.example.repository.interfaces

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import com.example.repository.datasources.room.entities.UserRequestRoomEntity

@Database(entities = [UserRequestRoomEntity::class], version = 1)
abstract class UserRequestDatabase: RoomDatabase(){
    abstract val contactDao: UserRequestDao

    companion object {
        const val DATABASE_NAME = "requests_db"
    }
}

@Dao
interface UserRequestDao {
    @Query("SELECT * FROM user_requests")
    suspend fun getAll(): List<UserRequestRoomEntity>

    @Query("SELECT * FROM user_requests WHERE id = :id")
    suspend fun getById(id: Int): UserRequestRoomEntity?

    @Query("DELETE FROM user_requests WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("UPDATE user_requests SET description = :description WHERE id = :id")
    suspend fun updateDescription(id: Int, description: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact: UserRequestRoomEntity)
}