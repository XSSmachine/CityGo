package com.example.domain.interfaces.userrequest_usecases

import com.hfad.model.UserRequestResponseModel

interface GetUserRequestUseCase {
    suspend fun execute(userId:Int,id: Int): UserRequestResponseModel?
}