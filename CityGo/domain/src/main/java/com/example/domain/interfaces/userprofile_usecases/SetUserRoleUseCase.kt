package com.example.domain.interfaces.userprofile_usecases

interface SetUserRoleUseCase {
    suspend fun execute(userRole:String)
}