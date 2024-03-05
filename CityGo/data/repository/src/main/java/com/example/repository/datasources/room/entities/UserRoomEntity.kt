package com.example.repository.datasources.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserRoomEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val email: String?,
    val phoneNumber: String
)
