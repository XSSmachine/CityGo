package com.example.domain.usecases.userrequest

import com.example.domain.interfaces.UserRequestRepository
import com.example.domain.interfaces.userrequest_usecases.DeleteUserRequestUseCase
import com.hfad.model.RepoResult

class DeleteUserRequestUseCaseImpl constructor(private val userRequestRepository: UserRequestRepository) : DeleteUserRequestUseCase{
    override suspend fun execute(userId:String,uuid: String): RepoResult<Unit> {
        return userRequestRepository.deleteUserRequest(userId,uuid)
    }
}