package com.example.domain.repositories

import android.app.Activity
import com.example.domain.interfaces.UserProfileRepository
import com.example.repository.interfaces.UsersDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.hfad.model.UserProfileRequestModel
import com.hfad.model.UserProfileResponseModel
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

 class UserProfileRepositoryImpl constructor(private val userProfileDataSource: UsersDataSource) :
    UserProfileRepository {



    override suspend fun createUser(name: String, surname: String, phoneNumber: String, email: String?) {
        // Create a new user profile with the provided data using the user profile data source
        return userProfileDataSource.createUserWithPhoneNumber(
            name = name,
            surname = surname,
            phoneNumber = phoneNumber,
            email = email,
            stars = 3.50
        )

    }

    override suspend fun userExists(phoneNumber: String): Boolean {
        // Check if a user with the given phone number exists using the user profile data source
        return userProfileDataSource.userExists(phoneNumber)
    }

    override suspend fun getUser(phoneNumber: String): UserProfileResponseModel? {
        // Retrieve user information based on the phone number using the user profile data source
        return userProfileDataSource.getUserByPhoneNumber(phoneNumber)
    }

    override suspend fun updateUser(userId: Int, name: String, surname: String, email: String?) {
        // Update user information with the provided data using the user profile data source
        // Assuming you have a way to retrieve the user's ID (userId) based on the phone number
        val userData = UserProfileRequestModel(
            name = name,
            surname = surname,
            phoneNumber = "", // Assuming phone number is not updated
            email = email,
            profilePicture = null, // Handle profile picture update if needed
            stars = 0.0 // Assuming stars is not updated
        )
        userProfileDataSource.updateUser(userId, userData)
    }

    override suspend fun deleteUser(phoneNumber: String) {
        // Delete the user account associated with the phone number using the user profile data source
        // Assuming you have a way to retrieve the user's ID based on the phone number
        val user = userProfileDataSource.getUserByPhoneNumber(phoneNumber)
        user?.let {
            userProfileDataSource.deleteUser(it.id)
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





