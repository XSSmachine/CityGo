package com.example.domain.usecases.userrequest

import com.example.domain.interfaces.UserRequestRepository
import com.example.domain.interfaces.userrequest_usecases.GetAllCurrentUserRequestsUseCase
import com.hfad.model.BasicError
import com.hfad.model.Failure
import com.hfad.model.RepoResult
import com.hfad.model.Success
import com.hfad.model.UserRequestResponseModel
import com.hfad.model.onFailure
import com.hfad.model.onSuccess
import kotlinx.coroutines.flow.Flow

class GetAllCurrentUserRequestsUseCaseImpl constructor(private val dao: UserRequestRepository
): GetAllCurrentUserRequestsUseCase {
    override suspend fun execute(userId: String): RepoResult<List<UserRequestResponseModel>> {
        try {
            dao.getAllUserRequestsForCurrentUser(userId)
            .onSuccess { return Success(it) }
                .onFailure { return Failure(it) }
        }catch (e: Exception) {
            e.printStackTrace()
            Failure(BasicError(Throwable("Get All Current User Requests Fetch failed")))
        }
        return Failure(BasicError(Throwable("Get All Current User Requests Fetch failed")))
    }
}