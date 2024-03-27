package com.example.domain.interfaces.userprofile_usecases

import kotlinx.coroutines.flow.Flow


interface ReadUserIdUseCase {
    suspend fun execute(): Flow<String?>
}