package com.example.domain.usecases.userrequest

import com.example.domain.interfaces.UserRequestRepository
import com.example.domain.interfaces.usecases.UpdateUserRequestUseCase
import com.hfad.model.UserRequestRequestModel

class UpdateUserRequestUseCaseImpl constructor(private val userRequestRepository: UserRequestRepository) : UpdateUserRequestUseCase {
    override suspend fun execute(id: Int, data: UserRequestRequestModel) {
        return userRequestRepository.updateContact(id,data)
    }
}