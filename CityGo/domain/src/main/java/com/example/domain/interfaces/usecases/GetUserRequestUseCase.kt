package com.example.domain.interfaces.usecases

import com.hfad.model.UserRequestResponseModel

interface GetUserRequestUseCase {
    suspend fun execute(id: Int): UserRequestResponseModel?
}