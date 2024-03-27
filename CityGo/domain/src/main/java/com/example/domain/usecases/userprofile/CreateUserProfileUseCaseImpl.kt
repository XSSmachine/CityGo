package com.example.domain.usecases.userprofile

import com.example.domain.interfaces.UserProfileRepository
import com.example.domain.interfaces.UserRequestRepository
import com.example.domain.interfaces.userprofile_usecases.CreateUserProfileUseCase
import com.example.domain.interfaces.userrequest_usecases.CreateUserRequestUseCase
import com.hfad.model.UserProfileRequestModel

class CreateUserProfileUseCaseImpl constructor(private val userProfileRepository: UserProfileRepository) :
    CreateUserProfileUseCase {
    override suspend fun execute(data: UserProfileRequestModel) {
        return userProfileRepository.createUser(data.name,data.surname,data.phoneNumber,data.email)
    }
}