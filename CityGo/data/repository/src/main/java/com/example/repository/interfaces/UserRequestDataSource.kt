package com.example.repository.interfaces

import com.hfad.model.UserRequestRequestModel
import com.hfad.model.UserRequestResponseModel


interface UserRequestDataSource {
    suspend fun getAll(userId: String): List<UserRequestResponseModel>

    suspend fun getAllForCurrentUser(userId: String): List<UserRequestResponseModel>

    suspend fun getOne(userId: String, id: Int): UserRequestResponseModel?
    suspend fun getOneById( id: Int): UserRequestResponseModel?

    suspend fun delete(userId: String, id: Int)

    suspend fun update(userId: String, id: Int, contact: UserRequestRequestModel)

    suspend fun create(data: UserRequestRequestModel)
}