package com.example.domain.usecases.userrequest

import com.example.domain.interfaces.UserRequestRepository
import com.example.domain.interfaces.userrequest_usecases.GetAllUserRequestsUseCase
import com.hfad.model.UserRequestResponseModel

class GetAllUserRequestsUseCaseImpl constructor(private val userRequestRepository: UserRequestRepository) : GetAllUserRequestsUseCase{
    override suspend fun execute(userId:Int): List<UserRequestResponseModel> {
        return userRequestRepository.getAllUserRequests(userId)
    }
}