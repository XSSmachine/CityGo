package com.example.network.interfaces.dao

import com.example.network.entities.UserRemoteEntity
import com.example.network.entities.UserRemoteResponseEntity
import com.hfad.model.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface RemoteUserDao {
    @GET("user.json")
    suspend fun getAllUsers(): Response<List<UserRemoteEntity>>

    @GET("user/{id}.json")
    suspend fun getSingleUserById(@Path("id") userId:String) : Response<UserRemoteEntity>

    @PUT("user/{ID}.json")
    suspend fun addUser(@Path("ID") userId:String, @Body updatedUserRemoteEntity: UserRemoteEntity): Response<UserRemoteEntity>

    @DELETE("user/{ID}.json")
    suspend fun deleteUser(@Path("id") userId:String): retrofit2.Response<Unit>

    @PATCH("user/{ID}.json")
    suspend fun updateUser(@Path("ID") id: String, @Body user:UserRemoteEntity) : Response<Unit>

    @PATCH("user/{ID}.json")
    @JvmSuppressWildcards
    suspend fun updateUserRequests(@Path("ID") id: String, @Body requests:Map<String,List<String>>) : retrofit2.Response<Unit>

    @GET("user.json?orderBy=\"PhoneNumber\"")
    suspend fun checkUserExists(@Query("equalTo") number: String): Response<Map<String, UserRemoteEntity>>

}