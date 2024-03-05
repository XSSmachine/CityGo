package com.example.domain.interfaces.usecases

import com.hfad.model.UserRequestResponseModel

interface GetAllUserRequestsUseCase {
    suspend fun execute(): List<UserRequestResponseModel>
}