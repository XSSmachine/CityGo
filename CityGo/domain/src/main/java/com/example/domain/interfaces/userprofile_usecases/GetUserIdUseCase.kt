package com.example.domain.interfaces.userprofile_usecases

import com.hfad.model.UserProfileResponseModel

interface GetUserIdUseCase {
    suspend fun execute(userId:String)
}