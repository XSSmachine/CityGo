package com.example.domain.interfaces.userrequest_usecases

interface DeleteUserRequestUseCase {
    suspend fun execute(userId:String,id: Int)
}