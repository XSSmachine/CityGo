package com.example.domain.interfaces.userrequest_usecases

import com.hfad.model.UserRequestRequestModel

interface UpdateUserRequestUseCase {
    suspend fun execute(userId:Int,id: Int, data: UserRequestRequestModel)
}