package com.example.domain.interfaces.userprofile_usecases

import com.hfad.model.UserProfileResponseModel
import com.hfad.model.UserRequestRequestModel

interface GetUserProfileUseCase {
    suspend fun execute(userId:String): UserProfileResponseModel?
}

