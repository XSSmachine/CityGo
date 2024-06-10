package com.example.domain.interfaces.userprofile_usecases

import com.hfad.model.RepoResult
import com.hfad.model.UserProfileResponseModel
import com.hfad.model.UserRequestRequestModel
import kotlinx.coroutines.flow.Flow

interface GetUserProfileUseCase {
    suspend fun execute(userId:String): RepoResult<UserProfileResponseModel>
}

