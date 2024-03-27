package com.example.repository.interfaces

import com.hfad.model.UserRequestRequestModel
import com.hfad.model.UserRequestResponseModel


interface UserRequestDataSource {
    suspend fun getAll(userId: Int): List<UserRequestResponseModel>

    suspend fun getOne(userId: Int, id: Int): UserRequestResponseModel?

    suspend fun delete(userId: Int, id: Int)

    suspend fun update(userId: Int, id: Int, contact: UserRequestRequestModel)

    suspend fun create(userId: Int, data: UserRequestRequestModel)
}