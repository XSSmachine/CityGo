package com.example.domain.interfaces.userrequest_usecases

import com.hfad.model.RepoResult
import com.hfad.model.UserRequestRequestModel

interface UpdateUserRequestUseCase {
    suspend fun execute(userId:String,uuid: String, data: UserRequestRequestModel): RepoResult<Unit>
}