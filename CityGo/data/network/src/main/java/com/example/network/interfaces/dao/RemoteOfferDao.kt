package com.example.network.interfaces.dao

import com.example.network.entities.OfferRemoteEntity
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



interface RemoteOfferDao {
    @GET("offer.json")
    suspend fun getAll(): Response<List<OfferRemoteEntity>>

//    @GET("offer/{ID}.json")
//    suspend fun getAllForCurrentProvider(@Path("ID") providerId:String): List<OfferRemoteEntity>

//    @GET("offer.json")
//    suspend fun checkIfOfferExists(@Query("orderBy") orderBy: String = "\"ServiceProviderID\"", @Query("equalTo") providerId: String,@Query("orderBy") orderByy: String = "\"RequestID\"", @Query("equalTo") requestId: String): List<OfferRemoteEntity>
//    @GET("offer.json")
//    suspend fun getAllForCurrentRequest(@Query("orderBy") orderBy: String = "\"UserRequestID\"", @Query("equalTo") requestId: String): List<OfferRemoteEntity>
    @GET("offer/{sid}.json")
    suspend fun getById(@Path("sid") sid: String): Response<OfferRemoteEntity>

    @DELETE("offer/{sid}.json")
    suspend fun deleteById(@Path("sid") sid: String): Response<Unit>

    @PUT("offer/{sid}.json")
    suspend fun updateUserRequest(
        @Path("sid") id: String,
        @Body request: OfferRemoteEntity
    ): retrofit2.Response<Unit>

    @PATCH("offer/{sid}.json")
    suspend fun updateOfferStatus(@Path("sid") sid: String, @Body status: Map<String,String>) : Response<Unit>

    @POST("offer.json")
    suspend fun addOffer(@Body updatedOfferRemoteEntity: OfferRemoteEntity): retrofit2.Response<ResponseBody>

    @GET("offer.json?orderBy=\"UserRequestID\"")
    suspend fun getOfferByUserRequestID(@Query("equalTo") userRequestId: String): Response<Map<String, OfferRemoteEntity>>
}