package com.example.domain.usecases.userrequest

import com.example.domain.interfaces.UserRequestRepository
import com.example.domain.interfaces.userrequest_usecases.UpdateUserRequestUseCase
import com.hfad.model.RepoResult
import com.hfad.model.UserRequestRequestModel

class UpdateUserRequestUseCaseImpl constructor(private val userRequestRepository: UserRequestRepository) : UpdateUserRequestUseCase {
    override suspend fun execute(userId:String,uuid: String, data: UserRequestRequestModel): RepoResult<Unit> {
        return userRequestRepository.updateUserRequest(userId,uuid,data)
    }
}