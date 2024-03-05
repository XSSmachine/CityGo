package com.example.domain.model


//dohvat iz baze
//data class UserRequestResponseModel(
//    val id: Int,
//    val userId: String,
//    val photos: List<String>,
//    val description: String,
//    val addresses: List<Address>,
//    val timeTable: List<TimeSlot>,
//    val category: Category,
//    val extraWorker: Boolean,
//    val price: Int
//)
//
////spremanje u bazu
//data class UserRequestRequestModel(
//    val id: Int? = null,
//    val userId: String,
//    val photos: List<String>,
//    val description: String,
//    val addresses: List<Address>,
//    val timeTable: List<TimeSlot>,
//    val category: Category,
//    val extraWorker: Boolean,
//    val price: Int
//)
//
//data class Address(
//    val addressName: String,
//    val latitude: Double?,
//    val longitude: Double?,
//    val liftStairs: Boolean,
//    val floor: Int,
//    val doorCode: String?, // Optional
//    val phoneNumber: String,
//)
//
//data class TimeSlot(
//    val startHour: Int,
//    val endHour: Int,
//    val isToday: Boolean
//)
//
//enum class Category {
//    SMALL,
//    MEDIUM,
//    LARGE
//}