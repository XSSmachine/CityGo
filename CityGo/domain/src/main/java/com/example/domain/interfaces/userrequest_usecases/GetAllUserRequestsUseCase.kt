package com.example.domain.interfaces.userrequest_usecases

import com.hfad.model.UserRequestResponseModel

interface GetAllUserRequestsUseCase {
    suspend fun execute(userId:Int): List<UserRequestResponseModel>
}