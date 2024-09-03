package com.example.network

import android.util.Log
import androidx.core.net.toUri
import com.example.network.interfaces.ImageUploader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class RemoteImageDataSource() : ImageUploader {
    private val storageRef: StorageReference = FirebaseStorage.getInstance().reference
    private val auth: FirebaseAuth= FirebaseAuth.getInstance();
    override suspend fun uploadImage(fileUri: String): String? {
        val imageRef: StorageReference = storageRef.child("images/${fileUri}")
        return try {
            Log.d("UPLOAD_IMAGE", "Starting upload process for file: $fileUri")

            val user = auth.currentUser
            if (user == null) {
                Log.e("UPLOAD_IMAGE", "User not authenticated")
                return null
            }
            Log.d("UPLOAD_IMAGE", "User authenticated: ${user.uid}")

            // Upload the image
            val uploadTask = imageRef.putFile(fileUri.toUri())

            suspendCoroutine { continuation ->
                uploadTask.addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        val url = uri.toString()
                        continuation.resume(url)
                    }.addOnFailureListener {
                        Log.e("UPLOAD_IMAGE", it.message ?: "Unknown error")
                        continuation.resume(null)
                    }
                }.addOnFailureListener {
                    Log.e("UPLOAD_IMAGE", it.message ?: "Unknown error")
                    continuation.resume(null)
                }
            }



        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    override suspend fun deleteImage(imageUrl: String): Boolean {
        return try {
            val imageRef: StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)
            imageRef.delete().await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}