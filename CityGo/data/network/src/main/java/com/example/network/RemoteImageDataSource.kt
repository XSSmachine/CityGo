package com.example.network

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.example.network.interfaces.ImageUploader
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import java.io.File

class RemoteImageDataSource() : ImageUploader {
    private val storageRef: StorageReference = FirebaseStorage.getInstance().reference

    override suspend fun uploadImage(fileUri: String): String? {
        val imageRef: StorageReference = storageRef.child("images/${fileUri}")
        return try {
            // Upload the image
            val uploadTask = imageRef.putFile(fileUri.toUri()).await()

            // Check if the upload was successful
            if (uploadTask.task.isSuccessful) {
                // Get the download URL
                val downloadUrl = imageRef.downloadUrl.await().toString()

                // Return the download URL
                downloadUrl
            } else {
                // If the upload was not successful, print an error message and return null
                Log.e("UPLOAD_IMAGE", "Failed to upload image")
                null
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