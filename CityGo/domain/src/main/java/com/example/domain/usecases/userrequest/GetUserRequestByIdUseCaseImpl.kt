package com.example.domain.usecases.userrequest

import com.example.domain.interfaces.UserRequestRepository
import com.example.domain.interfaces.userrequest_usecases.GetUserRequestByIdUseCase
import com.hfad.model.UserRequestResponseModel

class GetUserRequestByIdUseCaseImpl constructor(private val userRequestRepository: UserRequestRepository
): GetUserRequestByIdUseCase {
    override suspend fun execute(id: Int): UserRequestResponseModel? {
        return userRequestRepository.getUserRequestById(id)
    }
}