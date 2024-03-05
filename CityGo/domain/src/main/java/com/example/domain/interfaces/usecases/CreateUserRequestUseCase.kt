package com.example.domain.interfaces.usecases

import com.hfad.model.UserRequestRequestModel

interface CreateUserRequestUseCase {
    suspend fun execute(contact: UserRequestRequestModel)
}