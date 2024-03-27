package com.example.domain.interfaces.userrequest_usecases

import com.hfad.model.UserRequestRequestModel

interface CreateUserRequestUseCase {
    suspend fun execute(userId:Int, request : UserRequestRequestModel)
}