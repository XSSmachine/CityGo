package com.example.domain.interfaces.userprofile_usecases

import com.hfad.model.RepoResult
import com.hfad.model.UserRequestRequestModel

interface DeleteUserProfileUseCase {
    suspend fun execute(phoneNum:String): RepoResult<Unit>
}