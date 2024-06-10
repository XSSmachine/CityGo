package com.example.domain.usecases.userprofile

import android.util.Log
import com.example.domain.interfaces.UserProfileRepository
import com.example.domain.interfaces.userprofile_usecases.CheckIfUserProfileExistUseCase
import com.example.domain.interfaces.userprofile_usecases.CreateUserProfileUseCase
import com.hfad.model.BasicError
import com.hfad.model.Failure
import com.hfad.model.RepoResult
import com.hfad.model.Success
import com.hfad.model.onFailure
import com.hfad.model.onSuccess
import kotlinx.coroutines.flow.Flow

class CheckIfUserProfileExistUseCaseImpl constructor(private val userProfileRepository: UserProfileRepository) :
    CheckIfUserProfileExistUseCase {
    override suspend fun execute(phoneNum: String): RepoResult<Boolean> {
        Log.d("DEBUG", userProfileRepository.userExists(phoneNum).toString())
        try {
            userProfileRepository.userExists(phoneNum)
            .onSuccess { return Success(it) }
                .onFailure { return Failure(it)}
        }catch (e: Exception) {
            e.printStackTrace()
            Failure(BasicError(Throwable("User Profile Exists Fetch failed")))
        }
        return Failure(BasicError(Throwable("User Profile Exists Fetch failed")))
    }
}