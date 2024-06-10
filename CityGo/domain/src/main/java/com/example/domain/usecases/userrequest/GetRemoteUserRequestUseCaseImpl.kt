package com.example.domain.usecases.userrequest

import com.example.domain.interfaces.UserRequestRepository
import com.example.domain.interfaces.userrequest_usecases.GetRemoteUserRequestUseCase
import com.example.domain.interfaces.userrequest_usecases.GetUserRequestByIdUseCase
import com.hfad.model.BasicError
import com.hfad.model.Failure
import com.hfad.model.RepoResult
import com.hfad.model.Success
import com.hfad.model.UserRequestResponseModel
import com.hfad.model.onFailure
import com.hfad.model.onSuccess

class GetRemoteUserRequestUseCaseImpl constructor(private val userRequestRepository: UserRequestRepository
): GetRemoteUserRequestUseCase {
    override suspend fun execute(sid: String): RepoResult<UserRequestResponseModel> {
        try {
            userRequestRepository.getRemoteUserRequest(sid)
                .onSuccess { return Success(it) }
                .onFailure { return Failure(it) }
        }catch (e: Exception) {
            e.printStackTrace()
            Failure(BasicError(Throwable("Get User Request By Id Fetch failed")))
        }
        return Failure(BasicError(Throwable("Get User Request By Id Fetch failed")))
    }
}