package com.example.domain.usecases.userprofile

import com.example.domain.interfaces.DataStoreRepository
import com.example.domain.interfaces.userprofile_usecases.GetUserIdUseCase
import com.example.domain.interfaces.userprofile_usecases.SetUserIdUseCase

class GetUserIdUseCaseImpl constructor(private val dataStoreRepository: DataStoreRepository) :
    GetUserIdUseCase {
    override suspend fun execute(): Result<String> {
        return dataStoreRepository.getUserId()
    }

}