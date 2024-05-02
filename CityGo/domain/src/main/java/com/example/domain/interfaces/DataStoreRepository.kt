package com.example.domain.interfaces

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun setUserId(userId: String)
    suspend fun getUserId(): Result<String>
    suspend fun clearUserId()

    suspend fun setUserRole(userRole: String)
    suspend fun getUserRole(): Result<String>


}