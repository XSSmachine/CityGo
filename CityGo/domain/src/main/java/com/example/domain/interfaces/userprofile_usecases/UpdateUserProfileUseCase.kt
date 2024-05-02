package com.example.domain.interfaces.userprofile_usecases

import com.hfad.model.UserProfileRequestModel

interface UpdateUserProfileUseCase {
    suspend fun execute(userId:String, data: UserProfileRequestModel)
}