package com.example.repository.datasources.room.entities


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hfad.model.OfferRequestModel
import com.hfad.model.OfferResponseModel


@Entity(tableName = "offers")
data class OfferRoomEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "userRequest_id")
    val userRequestId: Int,
    @ColumnInfo(name = "serviceProvider_id")
    val serviceProviderId: String,
    val price: Int?,
    val timeTable: String?,
    val status: String

)

/**
 * Mapping functions
 * @author Karlo Kovačević
 */

//Useful when retrieving data from the database and
// mapping it to a model for presentation or usage in the UI
fun OfferRoomEntity.toOfferResponseModel(): OfferResponseModel {
    return OfferResponseModel(
        id = id!!,
        userRequestId =userRequestId,
        serviceProviderId = serviceProviderId,
        price =price,
        timeTable = timeTable,
        status = status,

    )
}

//This method is useful when preparing data to be stored in the database
fun OfferRequestModel.toOfferRoomEntity(): OfferRoomEntity{
    return OfferRoomEntity(
        id = id,
        userRequestId =userRequestId,
        serviceProviderId = serviceProviderId,
        price =price,
        timeTable = timeTable,
        status = status,
    )
}