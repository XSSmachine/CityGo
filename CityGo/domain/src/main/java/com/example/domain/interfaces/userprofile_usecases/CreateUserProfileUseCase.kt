package com.example.domain.interfaces.userprofile_usecases

import com.hfad.model.UserProfileRequestModel

interface CreateUserProfileUseCase {
    suspend fun execute(data:UserProfileRequestModel)
}