package com.example.domain.repositories

import com.example.domain.interfaces.UserRequestRepository
import com.hfad.model.UserRequestRequestModel
import com.hfad.model.UserRequestResponseModel
import com.example.repository.interfaces.UserRequestDataSource


class UserRequestRepositoryImpl constructor(private val userRequestDataSource: UserRequestDataSource) : UserRequestRepository {
    override suspend fun getAllUserRequests(userId: String): List<UserRequestResponseModel> {
        return userRequestDataSource.getAll(userId)
    }

    override suspend fun getAllUserRequestsForCurrentUser(userId:String): List<UserRequestResponseModel> {
        return userRequestDataSource.getAllForCurrentUser(userId)
    }

    override suspend fun getUserRequest(userId:String,id: Int): UserRequestResponseModel? {
        return userRequestDataSource.getOne(userId,id)
    }
    override suspend fun getUserRequestById(id: Int): UserRequestResponseModel? {
        return userRequestDataSource.getOneById(id)
    }

    override suspend fun deleteUserRequest(userId:String,id: Int) {
        return userRequestDataSource.delete(userId,id)
    }

    override suspend fun updateUserRequest(userId:String,id: Int, data: UserRequestRequestModel) {
        return userRequestDataSource.update(userId,id, data)
    }

    override suspend fun createUserRequest(data: UserRequestRequestModel) {
        return userRequestDataSource.create(data)
    }
}