package com.example.domain.interfaces.userrequest_usecases

import com.hfad.model.UserRequestResponseModel

interface GetAllCurrentUserRequestsUseCase {
    suspend fun execute(userId:String): List<UserRequestResponseModel>
}