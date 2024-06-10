package com.example.domain.repositories

import android.app.Activity
import android.util.Log

import androidx.core.net.toUri
import com.example.domain.interfaces.UserProfileRepository
import com.example.network.RemoteImageDataSource
import com.example.network.RemoteUserDataSource
import com.example.network.entities.UserRemoteEntity
import com.example.repository.datasources.room.entities.toUserProfileRequestModel
import com.example.repository.interfaces.UsersDataSource
import com.google.android.gms.common.api.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.hfad.model.BasicError
import com.hfad.model.Failure
import com.hfad.model.RepoResult
import com.hfad.model.Success
import com.hfad.model.UserProfileRequestModel
import com.hfad.model.UserProfileResponseModel
import com.hfad.model.onFailure
import com.hfad.model.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

 class UserProfileRepositoryImpl constructor(private val userProfileDataSource: UsersDataSource, private val remoteUserDataSource: RemoteUserDataSource, private val remoteImageDataSource: RemoteImageDataSource) :
    UserProfileRepository {


     override suspend fun createUser(userId:String, name: String, surname: String, phoneNumber: String, email: String?): RepoResult<Unit> {
         // Create a new user profile with the provided data using Firebase Realtime Database
         val result = remoteUserDataSource.createUser(userId,
             UserProfileRequestModel(userId,name,surname,email,phoneNumber,"",4.00,null,System.currentTimeMillis(),requests = emptyList())
         )

         // Check if the user was created successfully in Firebase
         if (result is Success) {
             val sid = result.data.id
//             Log.d("ROOM",sid)
             try {
                 // Save the user details in Room
                 userProfileDataSource.createUserWithPhoneNumber(
                     userId = userId,
                     name = name,
                     surname = surname,
                     phoneNumber = phoneNumber,
                     email = email,
                     sid = sid,
                     stars = 4.00
                 )
                 Log.d("ROOMDB", "User saved in Room DB")
                 return Success(Unit)
             } catch (e: Exception) {
                 Log.e("ROOMDB", "Exception during Room DB operation", e)
                 return Failure(BasicError(Throwable("Exception during Room DB operation")))
             }
         } else {
             Log.e("FIREBASE", "Failed to create user in Firebase")
             return Failure(BasicError(Throwable("Exception during Realtime DB operation")))

         }

     }


     override suspend fun userExists(phoneNumber: String):   RepoResult<Boolean>{
        // Check if a user with the given phone number exists using the user profile data source
        try {
            remoteUserDataSource.userExists(phoneNumber)
                .onSuccess { return Success(it.toString().isNotEmpty()) }
                .onFailure { return Failure(it) }
        }catch (e:Exception){
            e.printStackTrace()
            Failure(BasicError(Throwable("User Exists Fetch failed")))
        }
        return Failure(BasicError(Throwable("User Exists Fetch failed")))
    }

     override suspend fun getUser(userId: String): RepoResult<UserProfileResponseModel> {
         return try {
             // Fetch user from Firebase
             val firebaseResponse = remoteUserDataSource.getUserById(userId)

             firebaseResponse.onSuccess { firebaseUser ->
                 val firebaseUserProfile = firebaseUser

                 // Fetch user from Room
                 val localUserResponse = userProfileDataSource.getUserById(userId)

                 localUserResponse.onSuccess { localUser ->
                     // Compare sync values and update Room if necessary
                     if (firebaseUserProfile.sync!! > localUser.sync!!) {
                         // Update Room database with the more recent Firebase data
                         userProfileDataSource.updateUser(userId,firebaseUserProfile.toUserProfileRequestModel())

                     }else if (localUser.sync == null) {
                         // If the user doesn't exist in Room yet, save the Firebase data to Room
                         userProfileDataSource.createUserWithPhoneNumber(userId, firebaseUserProfile.phoneNumber, firebaseUserProfile.name, firebaseUserProfile.surname, firebaseUserProfile.email, firebaseUserProfile.stars, firebaseUserProfile.id)
                     }

                     // Return the most recent user data (local or firebase, whichever is newer)
                     Log.d("REALPROFILLL", localUser.toString())
                     Log.d("REALPROFILLL", firebaseUser.toString())
                     return  Success(if (firebaseUserProfile.sync!! > localUser.sync!!) firebaseUserProfile else localUser)
                 }.onFailure {
                     Log.d("REALPROFIL", "FAILED ROOM")
                     // If the local user fetch fails, we still want to proceed with Firebase data
                     // Update Room database with the Firebase data since local fetch failed
//                     userProfileDataSource.createUserWithPhoneNumber(userId,firebaseUserProfile.phoneNumber,firebaseUserProfile.name,firebaseUserProfile.surname,firebaseUserProfile.email,firebaseUserProfile.stars,firebaseUserProfile.id)
                     // Return the Firebase user data
                     userProfileDataSource.createUserWithPhoneNumber(userId, firebaseUserProfile.phoneNumber, firebaseUserProfile.name, firebaseUserProfile.surname, firebaseUserProfile.email, firebaseUserProfile.stars, firebaseUserProfile.id)
                     return Success(firebaseUserProfile)
                 }
             }.onFailure { error ->
                 // Handle failed Firebase response
                 Log.d("REALPROFIL", "FAILED FIREBASE")

                 val localUserResponse = userProfileDataSource.getUserById(userId)
                 localUserResponse.onSuccess { localUser ->

                     Log.d("REALPROFILLL", localUser.toString())

                     return  Success( localUser)
                 }.onFailure {
                     Log.d("REALPROFIL", "FAILED ROOM")
                     // If the local user fetch fails, we still want to proceed with Firebase data
                     // Update Room database with the Firebase data since local fetch failed
//                     userProfileDataSource.createUserWithPhoneNumber(userId,firebaseUserProfile.phoneNumber,firebaseUserProfile.name,firebaseUserProfile.surname,firebaseUserProfile.email,firebaseUserProfile.stars,firebaseUserProfile.id)
                     // Return the Firebase user data
                     return Failure(BasicError(Throwable("User doesnt exist")))
                 }
             }

             // If we reach here, something went wrong with the flow
             return Failure(BasicError(Throwable("Unexpected error occurred")))
         } catch (e: Exception) {
             Log.d("REALPROFIL", "FAILED FIREBASE")
             e.printStackTrace()
             return Failure(BasicError(e))
         }
     }



     override suspend fun updateUser(userId: String, name: String, surname: String, email: String, photo: String?): RepoResult<Unit> {

//             val imageUrl:String? = photo?.let { remoteImageDataSource.uploadImage(it) }
//             Log.d("PICTURF",imageUrl.toString())

//         val imageUrl = remoteImageDataSource.uploadImage(photo!!)
//
//         if (imageUrl == null) {
//             Log.e("FIREBASE", "Failed to upload image")
//             return Failure(BasicError(Throwable("Failed to upload image")))
//         }
         val userData = UserProfileRequestModel(
             id = userId,
             name = name,
             surname = surname,
             phoneNumber = "", // Assuming phone number is not updated
             email = email,
             profilePicture = photo, // Handle profile picture update if needed
             stars = 0.0, // Assuming stars is not updated
             sync = System.currentTimeMillis(),
             sid = null,
             requests = emptyList()
         )





             // Update user in Firebase

             val result = userProfileDataSource.updateUser(userId, userData)
             val updatedProfile = userProfileDataSource.getUserById(userId)


             updatedProfile.onSuccess {
                 val response = remoteUserDataSource.updateUser(userId,  it.toUserProfileRequestModel())
                 response.onSuccess { return Success(Unit) }
                     .onFailure {
                         Log.d("PICTURE","FAILURE")
                         return Failure(BasicError(Throwable("Failed to update room user"))) }
             }.onFailure {

                 Log.d("PICTURE", "FAILURE2: ${it.throwable}")
                 return Failure(BasicError(Throwable("Failed to update remote user")))
             }
             // Update user in Room



             // Return success if both updates are successful
             return Success(Unit)

     }


     override suspend fun deleteUser(userId: String): RepoResult<Unit> {
         return try {
             // Delete user in Firebase
             remoteUserDataSource.deleteUser(userId)

             // Delete user in Room
             userProfileDataSource.deleteUser(userId)

             // Return success if both deletions are successful
             Success(Unit)
         } catch (e: Exception) {
             // Handle any deletion failure
             Failure(BasicError(e))
         }
     }


     override fun logOut() {
        // Log out the current user using Firebase Authentication (if applicable)
        // This method may vary depending on the authentication mechanism you're using
        // For Firebase Authentication, you can call FirebaseAuth.getInstance().signOut()
    }

    override suspend fun initiatePhoneNumberAuthentication(
        number: String,
        auth: FirebaseAuth,
        activity: Activity,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

     override suspend fun verifyPhoneNumber(
         phoneNumber: String,
         verificationCode: String
     ): Boolean {
         return try {
             val credential = PhoneAuthProvider.getCredential(phoneNumber, verificationCode)
             val authResult = FirebaseAuth.getInstance().signInWithCredential(credential).await()
             authResult.user != null // Return true if user is not null, indicating successful verification
         } catch (e: Exception) {
             // Handle verification failure, e.g., log error or return false
             false
         }
     }
 }





