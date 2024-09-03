package com.example.repository.datasources.room.entities


import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.hfad.model.Address
import com.hfad.model.UserRequestRequestModel
import com.hfad.model.UserRequestResponseModel
import java.util.UUID

/**
 * This class represents the structure of the "user_requests" table in the database
 * @author Karlo Kovačević
 */
@Entity(
    tableName = "user_requests",
    primaryKeys = ["user_id", "uuid"],
    indices = [Index(value = ["sid"], unique = true)]
)
data class UserRequestRoomEntity(

    val uuid: String,
    @ColumnInfo(name = "user_id")
    val userId: String,
    val photo: String,
    val description: String,
    @Embedded(prefix = "address1_")
    val address1: Address,
    @Embedded(prefix = "address2_")
    val address2: Address,
    val timeTable: String,
    val date:String,
    val category: String,
    val extraWorker: Boolean,
    val price: Int,

    val sid: String?,
    val sync:Long?
)

/**
 * Mapping functions
 * @author Karlo Kovačević
 */

//Useful when retrieving data from the database and
// mapping it to a model for presentation or usage in the UI
fun UserRequestRoomEntity.toUserRequestResponseModel(): UserRequestResponseModel {
    return UserRequestResponseModel(
        uuid = uuid,
        userId =userId,
        photo = Uri.parse(photo),
        description =description,
        address1 = address1,
        address2 = address2,
        timeTable = timeTable,
        date=date,
        category = category,
        extraWorker =extraWorker,
        price = price,
        sid=sid,
        sync=sync,
        offers = null
    )
}

//This method is useful when preparing data to be stored in the database
fun UserRequestRequestModel.toUserRequestRoomEntity(): UserRequestRoomEntity{
    return UserRequestRoomEntity(
        uuid = (if (uuid.isNullOrEmpty()) UUID.randomUUID().toString() else uuid)!!,
        userId=userId,
        photo=photo!!,
        description=description,
        address1 = address1,
        address2 = address2,
        timeTable=timeTable,
        date=date,
        category=category,
        extraWorker=extraWorker,
        price=price,
        sid=sid,
        sync=sync,
    )
}

