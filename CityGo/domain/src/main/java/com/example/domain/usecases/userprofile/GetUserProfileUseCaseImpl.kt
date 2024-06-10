package com.example.domain.usecases.userprofile

import android.util.Log
import com.example.domain.interfaces.UserProfileRepository
import com.example.domain.interfaces.userprofile_usecases.CreateUserProfileUseCase
import com.example.domain.interfaces.userprofile_usecases.GetUserProfileUseCase
import com.hfad.model.BasicError
import com.hfad.model.Failure
import com.hfad.model.RepoResult
import com.hfad.model.Success
import com.hfad.model.UserProfileResponseModel
import com.hfad.model.onFailure
import com.hfad.model.onSuccess
import kotlinx.coroutines.flow.Flow

class GetUserProfileUseCaseImpl constructor(private val userProfileRepository: UserProfileRepository) :
    GetUserProfileUseCase {
    override suspend fun execute(userId: String): RepoResult<UserProfileResponseModel> {
        try {
            userProfileRepository.getUser(userId)
            .onSuccess {
                Log.d("REALPROFI", it.name)
                return Success(it) }
                .onFailure {
                    Log.d("REALPROFIL", it.errorCode.code.toString())
                    return Failure(it) }
        }catch (e: Exception) {
            e.printStackTrace()
            Failure(BasicError(Throwable("Get User Profile Fetch failed")))
        }
        return Failure(BasicError(Throwable("Get User Profile Fetch failed")))

    }
}