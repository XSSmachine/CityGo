package com.example.domain.usecases.userprofile

import android.util.Log
import com.example.domain.interfaces.UserProfileRepository
import com.example.domain.interfaces.userprofile_usecases.CheckIfUserProfileExistUseCase
import com.example.domain.interfaces.userprofile_usecases.CreateUserProfileUseCase

class CheckIfUserProfileExistUseCaseImpl constructor(private val userProfileRepository: UserProfileRepository) :
    CheckIfUserProfileExistUseCase {
    override suspend fun execute(phoneNum: String): Boolean {
        Log.d("DEBUG", userProfileRepository.userExists(phoneNum).toString())
        return userProfileRepository.userExists(phoneNum)
    }
}