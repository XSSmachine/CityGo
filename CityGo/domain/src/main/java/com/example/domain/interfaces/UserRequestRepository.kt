package com.example.domain.interfaces

import com.hfad.model.UserRequestRequestModel
import com.hfad.model.UserRequestResponseModel

interface UserRequestRepository {
    suspend fun getContacts(): List<UserRequestResponseModel>
    suspend fun getContact(id: Int): UserRequestResponseModel?
    suspend fun deleteContact(id: Int)
    suspend fun updateContact(id: Int, data: UserRequestRequestModel)
    suspend fun createContact(data: UserRequestRequestModel)
}