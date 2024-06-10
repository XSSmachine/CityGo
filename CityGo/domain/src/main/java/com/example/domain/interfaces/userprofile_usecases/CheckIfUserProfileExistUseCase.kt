package com.example.domain.interfaces.userprofile_usecases

import com.hfad.model.RepoResult
import com.hfad.model.UserRequestRequestModel
import kotlinx.coroutines.flow.Flow

interface CheckIfUserProfileExistUseCase {
    suspend fun execute(phoneNum:String): RepoResult<Boolean>
}