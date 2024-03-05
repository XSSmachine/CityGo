package com.example.domain.interfaces.usecases

import com.hfad.model.UserRequestRequestModel

interface UpdateUserRequestUseCase {
    suspend fun execute(id: Int, data: UserRequestRequestModel)
}