package com.example.domain.repositories

import android.util.Log
import com.example.domain.interfaces.ServiceProviderProfileRepository
import com.example.network.RemoteImageDataSource
import com.example.network.RemoteProviderDataSource
import com.example.network.entities.toServiceProviderProfileRequestModel
import com.example.repository.interfaces.ServiceProvidersDataSource
import com.hfad.model.BasicError
import com.hfad.model.Failure
import com.hfad.model.Progress
import com.hfad.model.RepoResult
import com.hfad.model.ServiceProviderProfileRequestModel
import com.hfad.model.ServiceProviderProfileResponseModel
import com.hfad.model.Success
import com.hfad.model.onFailure
import com.hfad.model.onSuccess
import kotlinx.coroutines.flow.Flow

class ServiceProviderProfileRepositoryImpl constructor(private val serviceProvidersDataSource: ServiceProvidersDataSource,
    private val remoteServiceProviderDataSource: RemoteProviderDataSource,
    private val remoteImageDataSource: RemoteImageDataSource
):ServiceProviderProfileRepository {
    override suspend fun getAllServiceProviders(): RepoResult<List<ServiceProviderProfileResponseModel>> {
        try {
            remoteServiceProviderDataSource.getAll()
                .onSuccess { return Success(it) }
                .onFailure { return Failure(it) }
        } catch (e: Exception) {
            e.printStackTrace()
            Failure(BasicError(Throwable("Getting All Service Providers Fetch failed")))
        }
        return Failure(BasicError(Throwable("Getting All Service Providers Fetch failed")))
    }

    override suspend fun getServiceProvider(cygoId: String): RepoResult<ServiceProviderProfileResponseModel> {
        try {
            val remoteResult = remoteServiceProviderDataSource.getById(cygoId)
                val localResult = serviceProvidersDataSource.getById(cygoId)


            val remoteData = when (remoteResult) {
                is Success -> remoteResult.data
                is Failure -> null
                is Progress -> TODO()
            }

            val localData = when (localResult) {
                is Success -> localResult.data
                is Failure -> null
                is Progress -> TODO()
            }

            if (remoteData != null) {
                // Compare and update local data if different
                if (localData == null || remoteData != localData) {
                    serviceProvidersDataSource.create(remoteData.toServiceProviderProfileRequestModel())//im getting error here -> Type mismatch.

                }
                return Success(remoteData)
            } else if (localData != null) {
                // If remote data is not available, update remote with local data
                remoteServiceProviderDataSource.create(localData.id,localData.toServiceProviderProfileRequestModel())//here too
                return Success(localData)
            } else {
                return Failure(BasicError(Throwable("No data available")))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return Failure(BasicError(Throwable("Fetching Service Provider failed")))
        }
    }


    override suspend fun deleteServiceProvider(cygoId: String): RepoResult<Unit> {
        return remoteServiceProviderDataSource.delete(cygoId)
    }

    override suspend fun updateServiceProvider(
        cygoId: String,
        data: ServiceProviderProfileRequestModel
    ): RepoResult<Unit> {
        return remoteServiceProviderDataSource.update(cygoId, data)
    }

    override suspend fun updateServiceProviderStatus(
        cygoId: String,
        status: String
    ): RepoResult<Unit> {
        val currentStatus = mapOf(Pair("Status",status))
        remoteServiceProviderDataSource.updateStatus(cygoId, currentStatus)
            .onSuccess {
                serviceProvidersDataSource.updateStatus(cygoId,status)
                    .onSuccess { return Success(Unit) }
                    .onFailure { return Failure(BasicError(Throwable("Failed to update")) ) }

            }
            .onFailure {
                serviceProvidersDataSource.updateStatus(cygoId,status)
                    .onSuccess { return Success(Unit) }
                    .onFailure { return Failure(BasicError(Throwable("Failed to update")) ) }
            }
        return Failure(BasicError(Throwable("Exception during execution!")))
    }

    override suspend fun createServiceProvider(data: ServiceProviderProfileRequestModel): RepoResult<Unit> {
        try {


            val profileImageUrl = remoteImageDataSource.uploadImage(data.profilePicture)

            if (profileImageUrl == null) {
                Log.e("FIREBASE", "Failed to upload profile image")
                return Failure(BasicError(Throwable("Failed to upload image")))
            }

            val idFrontImageUrl = remoteImageDataSource.uploadImage(data.idPictureFront)

            if (idFrontImageUrl == null) {
                Log.e("FIREBASE", "Failed to upload id front image")
                return Failure(BasicError(Throwable("Failed to upload image")))
            }

            val idBackImageUrl = remoteImageDataSource.uploadImage(data.idPictureBack)

            if (idBackImageUrl == null) {
                Log.e("FIREBASE", "Failed to upload id front image")
                return Failure(BasicError(Throwable("Failed to upload image")))
            }
            val vehicleImageUrl = remoteImageDataSource.uploadImage(data.vehiclePicture)

            if (vehicleImageUrl == null) {
                Log.e("FIREBASE", "Failed to upload id front image")
                return Failure(BasicError(Throwable("Failed to upload image")))
            }

            val updatedDataWithImages = data.copy(
                profilePicture = profileImageUrl,
                idPictureFront = idFrontImageUrl,
                idPictureBack = idBackImageUrl,
                vehiclePicture = vehicleImageUrl
                )


            remoteServiceProviderDataSource.create(updatedDataWithImages.id, updatedDataWithImages.copy(sync = System.currentTimeMillis()))
                .onSuccess {
                    val providerSaved = data.copy(sid = data.id, sync = System.currentTimeMillis())
                    serviceProvidersDataSource.create(data)
                        .onSuccess { return Success(Unit) }
                        .onFailure { return Failure(BasicError(Throwable("Exception during execution!"))) }
                }
                .onFailure {
                    serviceProvidersDataSource.create(data)
                        .onSuccess { return Success(Unit) }
                        .onFailure { return Failure(BasicError(Throwable("Exception during execution!"))) }
                    return Failure(BasicError(Throwable("Exception during execution!")))
                }

        } catch (e:Exception) {
            e.printStackTrace()
            Failure(BasicError(Throwable("Getting Service Provider Fetch failed")))

        }
        return Failure(BasicError(Throwable("Exception during execution!")))
    }
}