package com.example.repository.datasources.room.entities


import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hfad.model.Address
import com.hfad.model.UserRequestRequestModel
import com.hfad.model.UserRequestResponseModel

/**
 * This class represents the structure of the "user_requests" table in the database
 * @author Karlo Kovačević
 */
@Entity(tableName = "user_requests")
data class UserRequestRoomEntity(
    @PrimaryKey
    val id: Int? = null,
    @ColumnInfo(name = "user_id")
    val userId: String,
    val photo: String,
    val description: String,
    @Embedded(prefix = "address1_")
    val address1: Address,
    @Embedded(prefix = "address2_")
    val address2: Address,
    val timeTable: String,
    val category: String,
    val extraWorker: Boolean,
    val price: Int
)

/**
 * Mapping functions
 * @author Karlo Kovačević
 */

//Useful when retrieving data from the database and
// mapping it to a model for presentation or usage in the UI
fun UserRequestRoomEntity.toUserRequestResponseModel(): UserRequestResponseModel {
    return UserRequestResponseModel(
        id = id!!,
        userId =userId,
        photo = photo,
        description =description,
        address1 = address1,
        address2 = address2,
        timeTable = timeTable,
        category = category,
        extraWorker =extraWorker,
        price = price,
    )
}

//This method is useful when preparing data to be stored in the database
fun UserRequestRequestModel.toUserRequestRoomEntity(): UserRequestRoomEntity{
    return UserRequestRoomEntity(
        id = id,
        userId=userId,
        photo=photo,
        description=description,
        address1 = address1,
        address2 = address2,
        timeTable=timeTable,
        category=category,
        extraWorker=extraWorker,
        price=price,
    )
}

