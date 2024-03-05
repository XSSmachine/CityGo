package com.example.domain.usecases.userrequest

import com.example.domain.interfaces.UserRequestRepository
import com.example.domain.interfaces.usecases.GetUserRequestUseCase
import com.hfad.model.UserRequestResponseModel

class GetUserRequestUseCaseImpl constructor(private val userRequestRepository: UserRequestRepository) : GetUserRequestUseCase {
    override suspend fun execute(id: Int): UserRequestResponseModel? {
        return userRequestRepository.getContact(id)
    }
}