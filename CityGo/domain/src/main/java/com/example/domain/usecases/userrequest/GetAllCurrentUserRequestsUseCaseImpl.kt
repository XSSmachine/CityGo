package com.example.domain.usecases.userrequest

import com.example.domain.interfaces.UserRequestRepository
import com.example.domain.interfaces.userrequest_usecases.GetAllCurrentUserRequestsUseCase
import com.hfad.model.UserRequestResponseModel

class GetAllCurrentUserRequestsUseCaseImpl constructor(private val dao: UserRequestRepository
): GetAllCurrentUserRequestsUseCase {
    override suspend fun execute(userId: String): List<UserRequestResponseModel> {
        return dao.getAllUserRequestsForCurrentUser(userId)
    }
}