package com.example.domain.usecases.userprofile

import com.example.domain.interfaces.DataStoreRepository
import com.example.domain.interfaces.userprofile_usecases.SetUserRoleUseCase

class SetUserRoleUseCaseImpl constructor(private val dataStoreRepository: DataStoreRepository
):SetUserRoleUseCase {
    override suspend fun execute(userRole: String) {
        dataStoreRepository.setUserRole(userRole)
    }
}