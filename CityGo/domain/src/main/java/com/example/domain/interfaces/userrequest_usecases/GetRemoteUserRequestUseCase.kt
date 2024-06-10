package com.example.domain.interfaces.userrequest_usecases

import com.hfad.model.RepoResult
import com.hfad.model.UserRequestResponseModel

interface GetRemoteUserRequestUseCase {
    suspend fun execute(sid: String): RepoResult<UserRequestResponseModel>
}