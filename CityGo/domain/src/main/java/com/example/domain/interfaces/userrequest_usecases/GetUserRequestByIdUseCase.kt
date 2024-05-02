package com.example.domain.interfaces.userrequest_usecases

import com.hfad.model.UserRequestResponseModel

interface GetUserRequestByIdUseCase {
    suspend fun execute(id: Int): UserRequestResponseModel?

}