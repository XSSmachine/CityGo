package com.example.domain.usecases.userrequest

import com.example.domain.interfaces.UserRequestRepository
import com.example.domain.interfaces.userrequest_usecases.GetUserRequestUseCase
import com.hfad.model.UserRequestResponseModel

class GetUserRequestUseCaseImpl constructor(private val userRequestRepository: UserRequestRepository) : GetUserRequestUseCase {
    override suspend fun execute(userId:Int,id: Int): UserRequestResponseModel? {
        return userRequestRepository.getUserRequest(userId,id)
    }
}