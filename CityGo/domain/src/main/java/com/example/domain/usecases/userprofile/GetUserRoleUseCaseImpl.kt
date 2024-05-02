package com.example.domain.usecases.userprofile

import com.example.domain.interfaces.DataStoreRepository
import com.example.domain.interfaces.userprofile_usecases.GetUserRoleUseCase

class GetUserRoleUseCaseImpl constructor(private val dataStoreRepository: DataStoreRepository
):GetUserRoleUseCase {
    override suspend fun execute(): Result<String> {
        return dataStoreRepository.getUserRole()
    }
}