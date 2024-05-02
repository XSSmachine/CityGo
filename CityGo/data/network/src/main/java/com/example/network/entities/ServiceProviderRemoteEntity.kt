package com.example.network.entities

import com.hfad.model.ServiceProviderProfileRequestModel
import com.hfad.model.ServiceProviderProfileResponseModel


data class ServiceProviderProfileRemoteEntity(
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
        status=status)



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
        status=status
    )
}