package com.example.domain.usecases.userrequest

import com.example.domain.interfaces.UserRequestRepository
import com.example.domain.interfaces.userrequest_usecases.DeleteUserRequestUseCase

class DeleteUserRequestUseCaseImpl constructor(private val userRequestRepository: UserRequestRepository) : DeleteUserRequestUseCase{
    override suspend fun execute(userId:String,id: Int) {
        return userRequestRepository.deleteUserRequest(userId,id)
    }
}