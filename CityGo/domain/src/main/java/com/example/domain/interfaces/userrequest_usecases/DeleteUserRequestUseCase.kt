package com.example.domain.interfaces.userrequest_usecases

import com.hfad.model.RepoResult

interface DeleteUserRequestUseCase {
    suspend fun execute(userId:String,uuid: String): RepoResult<Unit>
}