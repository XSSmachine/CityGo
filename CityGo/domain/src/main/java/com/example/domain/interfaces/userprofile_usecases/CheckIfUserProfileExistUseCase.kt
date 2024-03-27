package com.example.domain.interfaces.userprofile_usecases

import com.hfad.model.UserRequestRequestModel

interface CheckIfUserProfileExistUseCase {
    suspend fun execute(phoneNum:String): Boolean
}