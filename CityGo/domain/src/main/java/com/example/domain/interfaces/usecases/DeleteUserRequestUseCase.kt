package com.example.domain.interfaces.usecases

interface DeleteUserRequestUseCase {
    suspend fun execute(id: Int)
}