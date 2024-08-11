package com.example.domain.usecases.userrequest

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.domain.interfaces.UserRequestRepository
import com.example.domain.interfaces.userrequest_usecases.CreateUserRequestUseCase
import com.hfad.model.RepoResult
import com.hfad.model.UserRequestRequestModel

class CreateUserRequestUseCaseImpl constructor(private val userRequestRepository: UserRequestRepository) : CreateUserRequestUseCase {
    override suspend fun execute( request: UserRequestRequestModel):RepoResult<Unit> {
        return userRequestRepository.createUserRequest(request)
    }




}