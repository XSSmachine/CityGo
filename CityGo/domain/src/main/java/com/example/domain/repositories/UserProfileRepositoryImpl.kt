package com.example.domain.repositories

import android.app.Activity
import android.util.Log

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
import kotlinx.coroutines.time.withTimeoutOrNull
import kotlinx.coroutines.withTimeoutOrNull
import java.util.concurrent.TimeUnit

class UserProfileRepositoryImpl constructor(
    private val userProfileDataSource: UsersDataSource,
    private val remoteUserDataSource: RemoteUserDataSource,
    private val remoteImageDataSource: RemoteImageDataSource
) :
    UserProfileRepository {


    override suspend fun createUser(
        userId: String,
        name: String,
        surname: String,
        phoneNumber: String,
        email: String?
    ): RepoResult<Unit> {
        // Create a new user profile with the provided data using Firebase Realtime Database
        val result = remoteUserDataSource.createUser(
            userId,
            UserProfileRequestModel(
                userId,
                name,
                surname,
                email,
                phoneNumber,
                "",
                4.00,
                null,
                System.currentTimeMillis(),
                requests = emptyList()
            )
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

                return Failure(BasicError(Throwable("Exception during Room DB operation")))
            }
        } else {
            return Failure(BasicError(Throwable("Exception during Realtime DB operation")))

        }

    }


    override suspend fun userExists(phoneNumber: String): RepoResult<Boolean> {
        // Check if a user with the given phone number exists using the user profile data source
        try {
            remoteUserDataSource.userExists(phoneNumber)
                .onSuccess { return Success(it.toString().isNotEmpty()) }
                .onFailure { return Failure(it) }
        } catch (e: Exception) {
            e.printStackTrace()
            Failure(BasicError(Throwable("User Exists Fetch failed")))
        }
        return Failure(BasicError(Throwable("User Exists Fetch failed")))
    }

    override suspend fun getUser(userId: String): RepoResult<UserProfileResponseModel> {
        return try {
            // First, try to fetch user from local database
            val localUserResponse = userProfileDataSource.getUserById(userId)
            localUserResponse.onSuccess {
                return localUserResponse
            }


            localUserResponse.onSuccess { localUser ->
                // Fetch user from Firebase to compare
                val firebaseResponse = remoteUserDataSource.getUserById(userId)

                firebaseResponse.onSuccess { firebaseUser ->
                    // Compare sync values and update local if necessary
                    if (firebaseUser.sync!! > localUser.sync!!) {
                        userProfileDataSource.updateUser(
                            userId,
                            firebaseUser.toUserProfileRequestModel()
                        )
                        return Success(firebaseUser)
                    } else {
                        return Success(localUser)
                    }
                }.onFailure { firebaseError ->
                    return Success(localUser)
                }
            }.onFailure { localError ->
                val firebaseResponse = remoteUserDataSource.getUserById(userId)

                firebaseResponse.onSuccess { firebaseUser ->
                    userProfileDataSource.createUserWithPhoneNumber(
                        userId,
                        firebaseUser.phoneNumber,
                        firebaseUser.name,
                        firebaseUser.surname,
                        firebaseUser.email,
                        firebaseUser.stars,
                        firebaseUser.id
                    )
                    return Success(firebaseUser)
                }.onFailure { firebaseError ->
                    return Failure(BasicError(Throwable("User doesn't exist")))
                }
            }

            // If we reach here, something unexpected happened
            return Failure(BasicError(Throwable("Unexpected error occurred")))
        } catch (e: Exception) {
            e.printStackTrace()
            return Failure(BasicError(e))
        }
    }


    override suspend fun updateUser(
        userId: String,
        name: String,
        surname: String,
        email: String,
        photo: String?
    ): RepoResult<Unit> {
        return try {
            var imageUrl: String = ""

            if (photo != null) {
                try {
                    val uploadResult = remoteImageDataSource.uploadImage(photo)
                    imageUrl = uploadResult ?: throw Exception("Failed to upload image")
                } catch (e: Exception) {
                    return Failure(BasicError(e))
                }
            }

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
                requests = emptyList(),
            )

            // Update user in Firebase
            userProfileDataSource.updateUser(userId, userData).onSuccess {
                Log.d("UPDATE_USER", "User updated successfully in Room")
            }.onFailure { e ->
                Log.e("UPDATE_USER", "Failed to update user in Room", Throwable(e.throwable))
                return Failure(BasicError(Throwable(e.throwable)))
            }

            val updatedProfile = userProfileDataSource.getUserById(userId.trim())
            updatedProfile.onSuccess { profile ->
                val updatedRequestModel =
                    profile.copy(profilePicture = imageUrl).toUserProfileRequestModel()
                val response = remoteUserDataSource.updateUser(userId, updatedRequestModel)
                response.onSuccess {
                    return Success(Unit)
                }.onFailure { e ->

                    return Failure(BasicError(Throwable(e.throwable)))
                }
            }.onFailure { e ->
                return Failure(BasicError(Throwable(e.throwable)))
            }

            Success(Unit)
        } catch (e: Exception) {
            Failure(BasicError(e))
        } catch (e: Exception) {
            Failure(BasicError(e))
        }
    }


    override suspend fun deleteUser(userId: String): RepoResult<Unit> {
        return try {
            remoteUserDataSource.deleteUser(userId)
            userProfileDataSource.deleteUser(userId)
            Success(Unit)
        } catch (e: Exception) {
            Failure(BasicError(e))
        }
    }

    override fun logOut() {}

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
            authResult.user != null
        } catch (e: Exception) {
            false
        }
    }
}





