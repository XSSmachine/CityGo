package com.example.domain.interfaces.userrequest_usecases

import com.hfad.model.RepoResult
import com.hfad.model.UserRequestResponseModel
import kotlinx.coroutines.flow.Flow

interface GetAllCurrentUserRequestsUseCase {
    suspend fun execute(userId:String): RepoResult<List<UserRequestResponseModel>>
}