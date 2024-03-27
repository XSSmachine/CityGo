package com.example.domain.repositories

import com.example.domain.interfaces.UserRequestRepository
import com.hfad.model.UserRequestRequestModel
import com.hfad.model.UserRequestResponseModel
import com.example.repository.interfaces.UserRequestDataSource


class UserRequestRepositoryImpl constructor(private val userRequestDataSource: UserRequestDataSource) : UserRequestRepository {
    override suspend fun getAllUserRequests(userId:Int): List<UserRequestResponseModel> {
        return userRequestDataSource.getAll(userId)
    }

    override suspend fun getUserRequest(userId:Int,id: Int): UserRequestResponseModel? {
        return userRequestDataSource.getOne(userId,id)
    }

    override suspend fun deleteUserRequest(userId:Int,id: Int) {
        return userRequestDataSource.delete(userId,id)
    }

    override suspend fun updateUserRequest(userId:Int,id: Int, data: UserRequestRequestModel) {
        return userRequestDataSource.update(userId,id, data)
    }

    override suspend fun createUserRequest(userId:Int,data: UserRequestRequestModel) {
        return userRequestDataSource.create(userId,data)
    }
}