package com.example.network.entities

import com.google.gson.annotations.SerializedName
import com.hfad.model.ServiceProviderProfileRequestModel
import com.hfad.model.ServiceProviderProfileResponseModel


data class ServiceProviderProfileRemoteEntity(
    @SerializedName("ID")
    val id: String,
    @SerializedName("Name")
    val name: String,
    @SerializedName("Surname")
    val surname: String,
    @SerializedName("Email")
    val email: String,
    @SerializedName("DateOfBirth")
    val dateOfBirth:String,
    @SerializedName("Address")
    val address: String,
    @SerializedName("Latitude")
    val latitude: Double?,
    @SerializedName("Longitude")
    val longitude: Double?,
    @SerializedName("ZipCode")
    val zipCode:String,
    @SerializedName("City")
    val city:String,
    @SerializedName("Country")
    val country:String,
    @SerializedName("PhoneNumber")
    val phoneNumber: String,
    @SerializedName("ProfilePicture")
    val profilePicture: String,
    @SerializedName("IdpictureFront")
    val idPictureFront:String,
    @SerializedName("IdPictureBack")
    val idPictureBack:String,
    @SerializedName("VehiclePicture")
    val vehiclePicture:String,
    @SerializedName("Stars")
    val stars: Double,
    @SerializedName("Status")
    val status:String,
    @SerializedName("SID")
    val sid: String?,
    @SerializedName("Sync")
    val sync:Long?,
    @SerializedName("Offers")
    val offers: List<String> = emptyList()


)

//Later think about adding statistics and separating sensitive personal data to different table for safety,
//also I need to change address data so its more performing...

/**
 * Mapping functions
 * @author Karlo Kovačević
 */

//Useful when retrieving data from the database and
// mapping it to a model for presentation or usage in the UI
fun ServiceProviderProfileRemoteEntity.toServiceProviderProfileResponseModel(): ServiceProviderProfileResponseModel {
    return ServiceProviderProfileResponseModel(
        id = id!!,
        name=name,
        surname= surname,
        email=email,
        dateOfBirth=dateOfBirth,
        address=address,
        latitude=latitude,
        longitude=longitude,
        zipCode=zipCode,
        city=city,
        country=country,
        phoneNumber=phoneNumber,
        profilePicture=profilePicture,
        idPictureFront=idPictureFront,
        idPictureBack=idPictureBack,
        vehiclePicture=vehiclePicture,
        stars=stars,
        status=status,
        sid=sid,
        sync=sync,
        offers = offers

    )



}

//This method is useful when preparing data to be stored in the database
fun ServiceProviderProfileRequestModel.toServiceProviderProfileRoomEntity(): ServiceProviderProfileRemoteEntity{
    return ServiceProviderProfileRemoteEntity(
        id = id,
        name=name,
        surname= surname,
        email=email,
        dateOfBirth=dateOfBirth,
        address=address,
        latitude=latitude,
        longitude=longitude,
        zipCode=zipCode,
        city=city,
        country=country,
        phoneNumber=phoneNumber,
        profilePicture=profilePicture,
        idPictureFront=idPictureFront,
        idPictureBack=idPictureBack,
        vehiclePicture=vehiclePicture,
        stars=stars,
        status=status,
        sid=sid,
        sync=sync,
        offers=offers ?: emptyList()

    )
}

fun ServiceProviderProfileResponseModel.toServiceProviderProfileRequestModel(): ServiceProviderProfileRequestModel{
    return ServiceProviderProfileRequestModel(
        id = id,
        name=name,
        surname= surname,
        email=email,
        dateOfBirth=dateOfBirth,
        address=address,
        latitude=latitude,
        longitude=longitude,
        zipCode=zipCode,
        city=city,
        country=country,
        phoneNumber=phoneNumber,
        profilePicture=profilePicture,
        idPictureFront=idPictureFront,
        idPictureBack=idPictureBack,
        vehiclePicture=vehiclePicture,
        stars=stars,
        status=status,
        sid=sid,
        sync=sync,
        offers=offers ?: emptyList()

    )
}