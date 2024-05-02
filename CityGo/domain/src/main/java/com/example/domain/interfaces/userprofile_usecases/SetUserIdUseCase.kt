package com.example.domain.interfaces.userprofile_usecases

interface SetUserIdUseCase {
    suspend fun execute(userId:String)
}