package com.hfad.model


//dohvat iz baze
data class UserRequestResponseModel(
    val id: Int,
    val userId: String,
    val photo: String,
    val description: String,
    val address1: Address,
    val address2: Address,
    val timeTable: String,
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
