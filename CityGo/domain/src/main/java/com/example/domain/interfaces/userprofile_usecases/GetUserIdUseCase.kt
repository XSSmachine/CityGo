package com.example.domain.interfaces.userprofile_usecases

interface GetUserIdUseCase {
    suspend fun execute():Result<String>
}