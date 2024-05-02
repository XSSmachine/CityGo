package com.hfad.model

import android.net.Uri


//dohvat iz baze
data class UserRequestResponseModel(
    val id: Int,
    val userId: String,
    val photo: Uri,
    val description: String,
    val address1: Address,
    val address2: Address,
    val timeTable: String,
    val date: String,
    val category: String,
    val extraWorker: Boolean,
    val price: Int
)

//spremanje u bazu
data class UserRequestRequestModel(
    val id: Int? = null,
    val userId: String,
    val photo: String,
    val description: String,
    val address1: Address,
    val address2: Address,
    val timeTable: String,
    val date: String,
    val category: String,
    val extraWorker: Boolean,
    val price: Int
)

data class Address(
    val addressName: String,
    val latitude: Double?,
    val longitude: Double?,
    val liftStairs: Boolean,
    val floor: Int,
    val doorCode: String?, // Optional
    val phoneNumber: String,
)

//-------------------------------------------------------------

data class UserProfileResponseModel(
    val id: String,
    val name: String,
    val surname: String,
    val email: String?,
    val phoneNumber: String,
    val profilePicture: String?,
    val stars: Double
)

data class UserProfileRequestModel(
    val id: String,
    val name: String,
    val surname: String,
    val email: String?,
    val phoneNumber: String,
    val profilePicture: String?,
    val stars: Double
)

//-------------------------------------------------------------
data class ServiceProviderProfileResponseModel(

    val id: String,
    val name: String,
    val surname: String,
    val email: String,
    val dateOfBirth:String,
    val address: String,
    val latitude: Double?,
    val longitude: Double?,
    val zipCode:String,
    val city:String,
    val country:String,
    val phoneNumber: String,
    val profilePicture: String,
    val idPictureFront:String,
    val idPictureBack:String,
    val vehiclePicture:String,
    val stars: Double,
    val status:String
)

data class ServiceProviderProfileRequestModel(

    val id: String,
    val name: String,
    val surname: String,
    val email: String,
    val dateOfBirth:String,
    val address: String,
    val latitude: Double?,
    val longitude: Double?,
    val zipCode:String,
    val city:String,
    val country:String,
    val phoneNumber: String,
    val profilePicture: String,
    val idPictureFront:String,
    val idPictureBack:String,
    val vehiclePicture:String,
    val stars: Double,
    val status:String
)


//----------------------------------------------------------------------

data class OfferResponseModel(
    val id: Int,
    val userRequestId: Int,
    val serviceProviderId: String,
    val price: Int?,
    val timeTable: String?,
    val status: String
)


data class OfferRequestModel(
    val id: Int?=null,
    val userRequestId: Int,
    val serviceProviderId: String,
    val price: Int?,
    val timeTable: String?,
    val status: String
)
