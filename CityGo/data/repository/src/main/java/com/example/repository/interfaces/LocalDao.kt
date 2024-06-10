package com.example.repository.interfaces

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import com.example.repository.datasources.room.entities.OfferRoomEntity
import com.example.repository.datasources.room.entities.ServiceProviderProfileRoomEntity
import com.example.repository.datasources.room.entities.UserProfileRoomEntity
import com.example.repository.datasources.room.entities.UserRequestRoomEntity
import kotlinx.coroutines.flow.Flow

/**
 * Room database class for the CityGo application.
 * Manages the entities and provides access to DAO interfaces.
 *
 * @author Karlo Kovačević
 */
@Database(entities = [UserRequestRoomEntity::class, UserProfileRoomEntity::class,ServiceProviderProfileRoomEntity::class,OfferRoomEntity::class], version = 6)
abstract class CityGoDatabase : RoomDatabase() {
    /**
     * Provides access to the DAO interface for UserRequestRoomEntity.
     */
    abstract val contactDao: UserRequestDao
    /**
     * Provides access to the DAO interface for UserProfileRoomEntity.
     */
    abstract val userDao: UserDao

    abstract val serviceProviderDao:ServiceProviderDao

    abstract val offerDao:OfferDao

    companion object {
        const val DATABASE_NAME = "citygo_db"
    }
}

/**
 * Data Access Object (DAO) interface for accessing and managing UserRequestRoomEntity entities.
 */
@Dao
interface UserRequestDao {
    /**
     * Retrieves all user requests from the database.
     */
    @Query("SELECT * FROM user_requests ORDER BY 1 desc")
    suspend fun getAll(): List<UserRequestRoomEntity>

    /**
     * Retrieves all user requests from the database for current user.
     */
    @Query("SELECT * FROM user_requests WHERE user_id=:userId ORDER BY 1 desc")
     suspend fun getAllForCurrentUser(userId:String): List<UserRequestRoomEntity>

    /**
     * Retrieves a user request by its ID from the database.
     */
    @Query("SELECT * FROM user_requests WHERE uuid = :uuid and user_id=:userId")
    suspend fun getByUserId(uuid: String,userId: String): UserRequestRoomEntity


    @Query("SELECT * FROM user_requests WHERE uuid = :id")
    suspend fun getById(id: String): UserRequestRoomEntity

    /**
     * Deletes a user request by its ID from the database.
     */
    @Query("DELETE FROM user_requests WHERE uuid = :uuid and user_id=:userId")
    suspend fun deleteById(uuid: String,userId: String)

    /**
     * Updates the description of a user request.
     */
    @Query("UPDATE user_requests SET photo=:photo, description = :description, address1_addressName=:address1Name, address1_latitude=:address1Latitude, address1_longitude=:address1Longitude, address1_liftStairs=:address1LiftStairs, address1_floor=:address1Floor, address1_doorCode=:address1DoorCode, address1_phoneNumber=:address1PhoneNumber, address2_addressName=:address2Name, address2_latitude=:address2Latitude, address2_longitude=:address2Longitude, address2_liftStairs=:address2LiftStairs, address2_floor=:address2Floor, address2_doorCode=:address2DoorCode, address2_phoneNumber=:address2PhoneNumber,timeTable=:timeTable,category=:category, extraWorker=:extraWorker,price=:price WHERE uuid = :uuid AND user_id=:userId")
    suspend fun updateUserRequest(
        uuid: String,
        userId: String,
        photo: String,
        description: String,
        address1Name: String,
        address1Latitude: Double?,
        address1Longitude: Double?,
        address1LiftStairs: Boolean,
        address1Floor: Int,
        address1DoorCode: String?,
        address1PhoneNumber: String,
        address2Name: String,
        address2Latitude: Double?,
        address2Longitude: Double?,
        address2LiftStairs: Boolean,
        address2Floor: Int,
        address2DoorCode: String?,
        address2PhoneNumber: String,
        timeTable:String,
        category:String,
        extraWorker:Boolean,
        price:Int
    )


    /**
     * Inserts a new user request into the database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(request: UserRequestRoomEntity)
}

/**
 * Data Access Object (DAO) interface for accessing and managing UserProfileRoomEntity entities.
 */
@Dao
interface UserDao {
    /**
     * Inserts or updates a user profile into the database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createUser(user: UserProfileRoomEntity)

    /**
     * Updates the profile information of a user in the database.
     */
    @Query("UPDATE users SET name = :name, surname = :surname, profilePicture= :profilePic WHERE id = :id")
    suspend fun updateUser(id: String, name: String, surname: String, profilePic: String)

    /**
     * Deletes a user by its ID from the database.
     */
    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUser(userId: String)

    /**
     * Retrieves a user profile by their phone number from the database.
     */
    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getById(userId: String): UserProfileRoomEntity

    /**
     * Checks if a user with the given phone number exists in the database.
     */
    @Query("SELECT EXISTS(SELECT * FROM users WHERE phoneNumber = :phoneNum)")
    suspend fun userExists(phoneNum: String): Boolean
}

@Dao
interface ServiceProviderDao {
    /**
     * Inserts or updates a service provider profile into the database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createServiceProvider(worker: ServiceProviderProfileRoomEntity)

    /**
     * Updates the profile information of a service provider in the database.
     */
    @Query("UPDATE workers SET name = :name, surname = :surname, email = :email,address=:street, dateOfBirth = :dateOfBirth, latitude = :latitude, longitude = :longitude,  zipCode = :zipCode, city = :city, country = :country, phoneNumber = :phoneNumber, profilePicture = :profilePicture, idPictureFront = :idPictureFront, idPictureBack = :idPictureBack, vehiclePicture = :vehiclePicture, stars = :stars, status = :status WHERE cygo_id = :id")
    suspend fun updateServiceProvider(
        id: String,
        name: String,
        surname: String,
        email: String,
        dateOfBirth: String,
        street: String,
        latitude: Double?,
        longitude: Double?,
        zipCode: String,
        city: String,
        country: String,
        phoneNumber: String,
        profilePicture: String,
        idPictureFront: String,
        idPictureBack: String,
        vehiclePicture: String,
        stars: Double,
        status: String
    )

    @Query("UPDATE workers SET status=:status WHERE cygo_id=:id")
    suspend fun updateStatus(
        id: String,
        status: String
    )


    /**
     * Deletes a service provider by its ID from the database.
     */
    @Query("DELETE FROM workers WHERE cygo_id = :userId")
    suspend fun deleteServiceProvider(userId: String)

    /**
     * Retrieves a service provider profile by their phone number from the database.
     */
    @Query("SELECT * FROM workers WHERE cygo_id = :cygoId")
    suspend fun getServiceProviderById(cygoId: String): ServiceProviderProfileRoomEntity

    /**
     * Checks if a service provider with the given phone number exists in the database.
     */
    @Query("SELECT * FROM workers")
    suspend fun getAllServiceProviders(): List<ServiceProviderProfileRoomEntity>
}


@Dao
interface OfferDao {
    /**
     * Inserts or updates a service provider profile into the database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createOffer(offer: OfferRoomEntity)

    /**
     * Updates the profile information of a service provider in the database.
     */
    @Query("UPDATE offers SET price = :price, timeTable = :timeTable, status = :status WHERE userRequest_uuid=:userRequestUIID and serviceProvider_id=:serviceProviderId")
    suspend fun updateOffer(
        userRequestUIID: String,
        serviceProviderId: String,
        price: Int?,
        timeTable: String,
        status: String
    )

    @Query("UPDATE offers SET status=:status WHERE userRequest_uuid=:userRequestUIID and serviceProvider_id=:serviceProviderId")
    suspend fun updateOfferStatus(
        userRequestUIID: String,
        serviceProviderId: String,
        status: String
    )

    @Query("SELECT EXISTS(SELECT * FROM offers WHERE userRequest_uuid = :requestUIID)")
    suspend fun offerExists(requestUIID: String): Boolean


    /**
     * Deletes a service provider by its ID from the database.
     */
    @Query("DELETE FROM offers WHERE userRequest_uuid=:userRequestUIID and serviceProvider_id=:serviceProviderId")
    suspend fun deleteOffer(
                                      userRequestUIID: String,
                                      serviceProviderId: String,)

    /**
     * Retrieves a service provider profile by their phone number from the database.
     */
    @Query("SELECT * FROM offers WHERE userRequest_uuid=:userRequestUIID and serviceProvider_id=:serviceProviderId")
    suspend fun getSingleOffer(userRequestUIID: String,
                                       serviceProviderId: String): OfferRoomEntity


    @Query("SELECT COUNT(*) FROM offers WHERE userRequest_uuid=:userRequestUIID AND serviceProvider_id=:serviceProviderId")
    suspend fun hasOffer(userRequestUIID: String, serviceProviderId: String): Int

    /**
     * Checks if a service provider with the given phone number exists in the database.
     */
    @Query("SELECT * FROM offers WHERE serviceProvider_id=:serviceProviderId ")
    suspend fun getAllMyOffers(serviceProviderId: String): List<OfferRoomEntity>

    @Query("SELECT * FROM offers WHERE userRequest_uuid=:userRequestUIID")
    suspend fun getAllOffers(userRequestUIID: String): List<OfferRoomEntity>
}

