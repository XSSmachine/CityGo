package com.example.domain.usecases.userprofile

import com.example.domain.interfaces.UserProfileRepository
import com.example.domain.interfaces.userprofile_usecases.CreateUserProfileUseCase
import com.example.domain.interfaces.userprofile_usecases.GetUserProfileUseCase
import com.hfad.model.UserProfileResponseModel

class GetUserProfileUseCaseImpl constructor(private val userProfileRepository: UserProfileRepository) :
    GetUserProfileUseCase {
    override suspend fun execute(userId: String): UserProfileResponseModel? {
        return userProfileRepository.getUser(userId)
    }
}