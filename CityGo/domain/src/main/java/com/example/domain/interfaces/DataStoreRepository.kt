package com.example.domain.interfaces

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun getUserId(userId: String)
    suspend fun clearUserId()
    val savedUserId: Flow<String>
}