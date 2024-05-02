package com.example.domain.usecases.userrequest

import com.example.domain.interfaces.UserRequestRepository
import com.example.domain.interfaces.userrequest_usecases.CreateUserRequestUseCase
import com.hfad.model.UserRequestRequestModel

class CreateUserRequestUseCaseImpl constructor(private val userRequestRepository: UserRequestRepository) : CreateUserRequestUseCase {
    override suspend fun execute( request: UserRequestRequestModel) {
        return userRequestRepository.createUserRequest(request)
    }


}