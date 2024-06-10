package com.example.network.interfaces

import java.io.File

interface ImageUploader {
    suspend fun uploadImage(fileUri: String): String?
    suspend fun deleteImage(imageUrl: String): Boolean
}