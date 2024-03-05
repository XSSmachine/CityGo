package com.example.domain.repositories

import com.example.domain.interfaces.UserRequestRepository
import com.hfad.model.UserRequestRequestModel
import com.hfad.model.UserRequestResponseModel
import com.example.repository.interfaces.UserRequestDataSource


class UserRequestRepositoryImpl constructor(private val userRequestDataSource: UserRequestDataSource) : UserRequestRepository {
    override suspend fun getContacts(): List<UserRequestResponseModel> {
        return userRequestDataSource.getAll()
    }

    override suspend fun getContact(id: Int): UserRequestResponseModel? {
        return userRequestDataSource.getOne(id)
    }

    override suspend fun deleteContact(id: Int) {
        return userRequestDataSource.delete(id)
    }

    override suspend fun updateContact(id: Int, data: UserRequestRequestModel) {
        return userRequestDataSource.update(id, data)
    }

    override suspend fun createContact(data: UserRequestRequestModel) {
        return userRequestDataSource.create(data)
    }
}