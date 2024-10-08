package com.example.domain.interfaces.userrequest_usecases

import com.hfad.model.RepoResult
import com.hfad.model.UserRequestRequestModel

interface CreateUserRequestUseCase {
    suspend fun execute(request : UserRequestRequestModel):RepoResult<Unit>
}