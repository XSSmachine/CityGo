package com.example.domain.usecases.userprofile

import com.example.domain.interfaces.UserProfileRepository
import com.example.domain.interfaces.userprofile_usecases.CreateUserProfileUseCase
import com.example.domain.interfaces.userprofile_usecases.UpdateUserProfileUseCase
import com.hfad.model.RepoResult
import com.hfad.model.UserProfileRequestModel

class UpdateUserProfileUseCaseImpl constructor(private val userProfileRepository: UserProfileRepository) :
    UpdateUserProfileUseCase {
    override suspend fun execute(userId: String, data: UserProfileRequestModel): RepoResult<Unit> {
        return userProfileRepository.updateUser(userId,data.name,data.surname,data.email!!,data.profilePicture)
    }
}