package com.example.network.interfaces.dao

import com.example.network.entities.ServiceProviderProfileRemoteEntity

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface RemoteProviderDao {
    @GET("provider.json")
    suspend fun getAllProviders(): Response<List<ServiceProviderProfileRemoteEntity>>

    @GET("provider/{id}.json")
    suspend fun getSingleProviderById(@Path("id") id: String) : Response<ServiceProviderProfileRemoteEntity>

    @PUT("provider/{ID}.json")
    suspend fun addProvider(@Path("ID") providerId:String, @Body updatedUserRemoteEntity: ServiceProviderProfileRemoteEntity): retrofit2.Response<ServiceProviderProfileRemoteEntity>

    @DELETE("provider/{id}.json")
    suspend fun deleteProvider(@Path("id") id: String): retrofit2.Response<Unit>

    @PUT("provider/{ID}.json")
    suspend fun updateProvider(@Path("ID") id: String, @Body user: ServiceProviderProfileRemoteEntity) : retrofit2.Response<Unit>

    @PATCH("provider/{ID}.json")
    suspend fun updateProviderStatus(@Path("ID") id: String, @Body status: Map<String,String>) : retrofit2.Response<Unit>

    @PATCH("provider/{ID}.json")
    @JvmSuppressWildcards
    suspend fun updateOffers(@Path("ID") userId: String,@Body offersMap: Map<String, List<String>>): retrofit2.Response<Unit>

}