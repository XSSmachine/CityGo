package com.example.domain.usecases.userprofile

import com.example.domain.interfaces.DataStoreRepository
import com.example.domain.interfaces.userprofile_usecases.SetUserIdUseCase

class SetUserIdUseCaseImpl constructor(private val dataStoreRepository: DataStoreRepository) :
    SetUserIdUseCase {
    override suspend fun execute(userId: String) {
        dataStoreRepository.setUserId(userId)
    }
}