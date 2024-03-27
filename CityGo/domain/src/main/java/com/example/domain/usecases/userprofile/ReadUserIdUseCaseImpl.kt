package com.example.domain.usecases.userprofile

import com.example.domain.interfaces.DataStoreRepository
import com.example.domain.interfaces.UserProfileRepository
import com.example.domain.interfaces.userprofile_usecases.GetUserProfileUseCase
import com.example.domain.interfaces.userprofile_usecases.ReadUserIdUseCase
import kotlinx.coroutines.flow.Flow

class ReadUserIdUseCaseImpl constructor(private val dataStoreRepository: DataStoreRepository) :
    ReadUserIdUseCase {
    override suspend fun execute(): Flow<String> {
        return dataStoreRepository.savedUserId
    }
}