package com.example.domain.interfaces.userrequest_usecases

import com.hfad.model.UserRequestResponseModel

interface GetUserRequestUseCase {
    suspend fun execute(userId:String,id: Int): UserRequestResponseModel?
}