package com.example.domain.usecases.userprofile

import com.example.domain.interfaces.UserProfileRepository
import com.example.domain.interfaces.userprofile_usecases.CreateUserProfileUseCase
import com.example.domain.interfaces.userprofile_usecases.DeleteUserProfileUseCase
import com.hfad.model.RepoResult

class DeleteUserProfileUseCaseImpl constructor(private val userProfileRepository: UserProfileRepository) :
    DeleteUserProfileUseCase {
    override suspend fun execute(phoneNum: String): RepoResult<Unit> {
        return userProfileRepository.deleteUser(phoneNum)
    }
}