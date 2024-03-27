package com.example.domain.usecases.userprofile

import com.example.domain.interfaces.DataStoreRepository
import com.example.domain.interfaces.userprofile_usecases.ClearUserIdUseCase
import com.example.domain.interfaces.userprofile_usecases.ReadUserIdUseCase

class ClearUserIdUseCaseImpl constructor(private val dataStoreRepository: DataStoreRepository) :
    ClearUserIdUseCase {
    override suspend fun execute() {
        dataStoreRepository.clearUserId()
    }
}