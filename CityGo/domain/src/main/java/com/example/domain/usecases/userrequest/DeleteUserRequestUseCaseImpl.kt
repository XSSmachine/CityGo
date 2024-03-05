package com.example.domain.usecases.userrequest

import com.example.domain.interfaces.UserRequestRepository
import com.example.domain.interfaces.usecases.DeleteUserRequestUseCase

class DeleteUserRequestUseCaseImpl constructor(private val userRequestRepository: UserRequestRepository) : DeleteUserRequestUseCase{
    override suspend fun execute(id: Int) {
        return userRequestRepository.deleteContact(id)
    }
}