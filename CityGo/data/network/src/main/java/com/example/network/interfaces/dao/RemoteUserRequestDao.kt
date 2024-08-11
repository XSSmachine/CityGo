package com.example.network.interfaces.dao

import com.example.network.entities.UserRemoteEntity
import com.example.network.entities.UserRequestRemoteEntity
import com.hfad.model.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface RemoteUserRequestDao {


        @GET("requests.json")
        suspend fun getAll(): Response<Map<String, UserRequestRemoteEntity>>

        @GET("requests.json")
        suspend fun getAllForCurrentUser(@Query("orderBy") orderBy: String = "\"UserID\"", @Query("equalTo") userId: String): Response<Map<String, UserRequestRemoteEntity>>

        @GET("requests/{sid}.json")
        suspend fun getById(@Path("sid") sid: String): retrofit2.Response<UserRequestRemoteEntity>


        @DELETE("requests/{sid}.json")
        suspend fun deleteById(@Path("sid") sid: String): retrofit2.Response<Unit>

        @PATCH("requests/{sid}.json")
        suspend fun updateUserRequest(
            @Path("sid") sid: String,
            @Body request: UserRequestRemoteEntity
        ): retrofit2.Response<Unit>

        @POST("requests.json")
        suspend fun addUserRequest( @Body updatedUserRequestRemoteEntity: UserRequestRemoteEntity): Response<ResponseBody>

        @PATCH("requests/{sid}.json")
        @JvmSuppressWildcards
        suspend fun updateOffers(@Path("sid") sid: String, @Body offers: Map<String, List<String>>): Response<Unit>
}