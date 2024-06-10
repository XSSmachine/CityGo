package com.example.domain.usecases.userrequest

import com.example.domain.interfaces.UserRequestRepository
import com.example.domain.interfaces.userrequest_usecases.GetAllUserRequestsUseCase
import com.hfad.model.BasicError
import com.hfad.model.Failure
import com.hfad.model.RepoResult
import com.hfad.model.Success
import com.hfad.model.UserRequestResponseModel
import com.hfad.model.onFailure
import com.hfad.model.onSuccess
import kotlinx.coroutines.flow.Flow

class GetAllUserRequestsUseCaseImpl constructor(private val userRequestRepository: UserRequestRepository) : GetAllUserRequestsUseCase{
    override suspend fun execute(): RepoResult<List<UserRequestResponseModel>> {
        try {
            userRequestRepository.getAllUserRequests()
            .onSuccess { return Success(it) }
                .onFailure { return Failure(it) }
        }catch (e: Exception) {
            e.printStackTrace()
            Failure(BasicError(Throwable("Get All User Requests Fetch failed")))
        }
        return Failure(BasicError(Throwable("Get All User Requests Fetch failed")))
    }
}