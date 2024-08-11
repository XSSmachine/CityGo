package com.hfad.model

import android.net.Uri
import com.google.gson.annotations.SerializedName
import java.io.Serial


//dohvat iz baze
data class UserRequestResponseModel(
    val uuid: String?,
    val userId: String,
    val photo: Uri,
    val description: String,
    var address1: Address,
    val address2: Address,
    val timeTable: String,
    val date: String,
    val category: String,
    val extraWorker: Boolean,
    val price: Int,

    val sid: String?,
    val sync:Long?,
    val offers: List<String>?
)

//spremanje u bazu
data class UserRequestRequestModel(
    val uuid: String?,
    val userId: String,
    val photo: String,
    val description: String,
    val address1: Address,
    val address2: Address,
    val timeTable: String,
    val date: String,
    val category: String,
    val extraWorker: Boolean,
    val price: Int,

    val sid: String?,
    val sync:Long?,
    val offers: List<String>?
)

fun UserRequestResponseModel.toRequestModel(): UserRequestRequestModel {
    return UserRequestRequestModel(
        uuid = this.uuid,
        userId = this.userId,
        photo = this.photo.toString(), // Convert Uri to String
        description = this.description,
        address1 = this.address1,
        address2 = this.address2,
        timeTable = this.timeTable,
        date = this.date,
        category = this.category,
        extraWorker = this.extraWorker,
        price = this.price,
        sid = this.sid,
        sync = this.sync,
        offers = this.offers
    )
}


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
    val stars: Double,
    val sid: String?,
    val sync:Long?,
    val requests: List<String>?
)

data class UserProfileRequestModel(
    val id: String,
    val name: String,
    val surname: String,
    val email: String?,
    val phoneNumber: String,
    val profilePicture: String?,
    val stars: Double,
    val sid: String?,
    val sync:Long?,
    val requests: List<String>?
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
    val status:String,

    val sid: String?,
    val sync:Long?,
    val offers: List<String>?
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
    val status:String,

    val sid: String?,
    val sync:Long?,
    val offers: List<String>?

)


//----------------------------------------------------------------------

data class OfferResponseModel(
    val id: Int,
    val userRequestUUID: String,
    val serviceProviderId: String,
    val price: Int?,
    val timeTable: String?,
    val status: String,

    val sid: String?,
    val sync:Long?
)


data class OfferRequestModel(
    val id: Int?=null,
    val userRequestUUID: String,
    val serviceProviderId: String,
    val price: Int?,
    val timeTable: String?,
    val status: String,

    val sid: String?,
    val sync:Long?
)


//-----------------------------------------------------------------------

data class ResponseBody(
    @SerializedName("name")
    val sid: String
)
