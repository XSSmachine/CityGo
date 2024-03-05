package com.example.repository.interfaces

import com.hfad.model.UserRequestRequestModel
import com.hfad.model.UserRequestResponseModel


interface UserRequestDataSource {
    suspend fun getAll(): List<UserRequestResponseModel>

    suspend fun getOne(id: Int): UserRequestResponseModel?

    suspend fun delete(id: Int)

    suspend fun update(id: Int, contact: UserRequestRequestModel)

    suspend fun create(data: UserRequestRequestModel)
}