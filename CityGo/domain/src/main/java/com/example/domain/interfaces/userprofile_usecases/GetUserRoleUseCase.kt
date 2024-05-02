package com.example.domain.interfaces.userprofile_usecases

interface GetUserRoleUseCase {
    suspend fun execute():Result<String>

}