package com.example.citygo

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.example.domain.interfaces.DataStoreRepository
import com.example.domain.interfaces.OfferRepository
import com.example.domain.interfaces.ServiceProviderProfileRepository
import com.example.domain.interfaces.UserProfileRepository
import com.example.domain.interfaces.UserRequestRepository
import com.example.domain.interfaces.offer_usecases.CheckCreatedOffersUseCase
import com.example.domain.interfaces.offer_usecases.CreateOfferUseCase
import com.example.domain.interfaces.offer_usecases.DeleteOfferUseCase
import com.example.domain.interfaces.offer_usecases.GetAllMyOffersUseCase
import com.example.domain.interfaces.offer_usecases.GetAllOffersUseCase
import com.example.domain.interfaces.offer_usecases.GetOfferByUserRequestUseCase
import com.example.domain.interfaces.offer_usecases.GetOfferUseCase
import com.example.domain.interfaces.offer_usecases.HasOfferUseCase
import com.example.domain.interfaces.offer_usecases.UpdateOfferStatusUseCase
import com.example.domain.interfaces.serviceproviderprofile_usecases.CreateServiceProviderProfileUseCase
import com.example.domain.interfaces.serviceproviderprofile_usecases.DeleteServiceProviderProfileUseCase
import com.example.domain.interfaces.serviceproviderprofile_usecases.GetServiceProviderProfileUseCase
import com.example.domain.interfaces.serviceproviderprofile_usecases.UpdateServiceProviderStatusUseCase
import com.example.domain.interfaces.userprofile_usecases.CheckIfUserProfileExistUseCase
import com.example.domain.interfaces.userprofile_usecases.ClearUserIdUseCase
import com.example.domain.interfaces.userprofile_usecases.CreateUserProfileUseCase
import com.example.domain.interfaces.userprofile_usecases.GetUserIdUseCase
import com.example.domain.interfaces.userprofile_usecases.SetUserIdUseCase
import com.example.domain.interfaces.userprofile_usecases.GetUserProfileUseCase
import com.example.domain.interfaces.userprofile_usecases.GetUserRoleUseCase
import com.example.domain.interfaces.userprofile_usecases.SendVerificationCodeUseCase
import com.example.domain.interfaces.userprofile_usecases.SetUserRoleUseCase
import com.example.domain.interfaces.userprofile_usecases.UpdateUserProfileUseCase
import com.example.domain.interfaces.userrequest_usecases.CreateUserRequestUseCase
import com.example.domain.interfaces.userrequest_usecases.DeleteUserRequestUseCase
import com.example.domain.interfaces.userrequest_usecases.GetAllCurrentUserRequestsUseCase
import com.example.domain.interfaces.userrequest_usecases.GetAllUserRequestsUseCase
import com.example.domain.interfaces.userrequest_usecases.GetRemoteUserRequestUseCase
import com.example.domain.interfaces.userrequest_usecases.GetUserRequestByIdUseCase
import com.example.domain.interfaces.userrequest_usecases.GetUserRequestUseCase
import com.example.domain.interfaces.userrequest_usecases.UpdateUserRequestUseCase
import com.example.domain.repositories.DataStoreRepositoryImpl
import com.example.domain.repositories.OfferRepositoryImpl
import com.example.domain.repositories.ServiceProviderProfileRepositoryImpl
import com.example.domain.repositories.UserProfileRepositoryImpl
import com.example.domain.repositories.UserRequestRepositoryImpl
import com.example.domain.usecases.offer.CheckCreatedOffersUseCaseImpl
import com.example.domain.usecases.offer.CreateOfferUseCaseImpl
import com.example.domain.usecases.offer.DeleteOfferUseCaseImpl
import com.example.domain.usecases.offer.GetAllMyOffersUseCaseImpl
import com.example.domain.usecases.offer.GetAllOffersUseCaseImpl
import com.example.domain.usecases.offer.GetOfferByUserRequestUseCaseImpl
import com.example.domain.usecases.offer.GetOfferUseCaseImpl
import com.example.domain.usecases.offer.HasOfferUseCaseImpl
import com.example.domain.usecases.offer.UpdateOfferStatusUseCaseImpl
import com.example.domain.usecases.serviceproviderprofile.CreateServiceProviderProfileUseCaseImpl
import com.example.domain.usecases.serviceproviderprofile.DeleteServiceProviderProfileUseCaseImpl
import com.example.domain.usecases.serviceproviderprofile.GetServiceProviderProfileUseCaseImpl
import com.example.domain.usecases.serviceproviderprofile.UpdateServiceProviderStatusUseCaseImpl
import com.example.domain.usecases.userprofile.CheckIfUserProfileExistUseCaseImpl
import com.example.domain.usecases.userprofile.ClearUserIdUseCaseImpl
import com.example.domain.usecases.userprofile.CreateUserProfileUseCaseImpl
import com.example.domain.usecases.userprofile.GetUserIdUseCaseImpl
import com.example.domain.usecases.userprofile.SetUserIdUseCaseImpl
import com.example.domain.usecases.userprofile.GetUserProfileUseCaseImpl
import com.example.domain.usecases.userprofile.GetUserRoleUseCaseImpl
import com.example.domain.usecases.userprofile.SendVerificationCodeUseCaseImpl
import com.example.domain.usecases.userprofile.SetUserRoleUseCaseImpl
import com.example.domain.usecases.userprofile.UpdateUserProfileUseCaseImpl
import com.example.domain.usecases.userrequest.CreateUserRequestUseCaseImpl
import com.example.domain.usecases.userrequest.DeleteUserRequestUseCaseImpl
import com.example.domain.usecases.userrequest.GetAllCurrentUserRequestsUseCaseImpl
import com.example.domain.usecases.userrequest.GetAllUserRequestsUseCaseImpl
import com.example.domain.usecases.userrequest.GetRemoteUserRequestUseCaseImpl
import com.example.domain.usecases.userrequest.GetUserRequestByIdUseCaseImpl
import com.example.domain.usecases.userrequest.GetUserRequestUseCaseImpl
import com.example.domain.usecases.userrequest.UpdateUserRequestUseCaseImpl
import com.example.network.RemoteImageDataSource
import com.example.network.RemoteOfferDataSource
import com.example.network.RemoteProviderDataSource
import com.example.network.RemoteUserDataSource
import com.example.network.RemoteUserRequestDataSource
import com.example.network.interfaces.ImageUploader
import com.example.network.interfaces.dao.RemoteOfferDao
import com.example.network.interfaces.dao.RemoteProviderDao
import com.example.network.interfaces.dao.RemoteUserDao
import com.example.network.interfaces.dao.RemoteUserRequestDao
import com.example.repository.datasources.room.RoomOfferDataSource
import com.example.repository.datasources.room.RoomServiceProviderDataSource
import com.example.repository.datasources.room.RoomUserDataSource
import com.example.repository.datasources.room.RoomUserRequestDataSource
import com.example.repository.interfaces.UserRequestDataSource
import com.example.repository.interfaces.CityGoDatabase
import com.example.repository.interfaces.OfferDataSource
import com.example.repository.interfaces.ServiceProvidersDataSource
import com.example.repository.interfaces.UsersDataSource
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


/**
 * This is Dagger module which is responsible for providing various dependencies
 * required by my application using Dagger's DI framework.
 * @author Karlo Kovačević
 */


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * We use @Provides annotation to specify instances of certain types which Dagger enables us to inject
     * this instance into other classes effectively. In this first example we get implementation of 'UserRequestDataSource' interface
     * which interacts with Room database to manage user request data.
     */
    @Provides
    @Singleton
    fun providesContactDatasource(
        @ApplicationContext
        context: Context
    ): UserRequestDataSource {
        return RoomUserRequestDataSource(
            dao = Room.databaseBuilder(
                context,
                CityGoDatabase::class.java,
                CityGoDatabase.DATABASE_NAME
            ).build().contactDao
            //the build() method is called to create the database instance,
            // and then the contactDao property is accessed to obtain the DAO
            // (Data Access Object) interface for the UserRequest entity.
        )
    }

    @Provides
    fun providesRetrofitUserDao(retrofit: Retrofit): RemoteUserDao {
        return retrofit.create(RemoteUserDao::class.java)
    }

    @Provides
    fun provideRemoteUserDataSource(dao: RemoteUserDao): RemoteUserDataSource {
        return RemoteUserDataSource(dao)
    }

    @Provides
    fun provideRemoteImageDataSource() : RemoteImageDataSource {
        return RemoteImageDataSource()
    }

    @Provides
    fun provideRemoteOfferDataSource(dao:RemoteOfferDao) : RemoteOfferDataSource {
        return RemoteOfferDataSource(dao)
    }
    @Provides
    fun provideRemoteUserRequestDataSource(dao: RemoteUserRequestDao): RemoteUserRequestDataSource {
        return RemoteUserRequestDataSource(dao)
    }
    @Provides
    fun provideRemoteProviderDataSource(dao: RemoteProviderDao): RemoteProviderDataSource {
        return RemoteProviderDataSource(dao)
    }


    @Provides
    fun providesRetrofitUserRequestDao(retrofit: Retrofit): RemoteUserRequestDao {
        return retrofit.create(RemoteUserRequestDao::class.java)
    }

    @Provides
    fun providesRetrofitOfferDao(retrofit: Retrofit): RemoteOfferDao {
        return retrofit.create(RemoteOfferDao::class.java)
    }

    @Provides
    fun providesRetrofitProviderDao(retrofit: Retrofit): RemoteProviderDao {
        return retrofit.create(RemoteProviderDao::class.java)
    }

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .baseUrl("https://citygo-35101-default-rtdb.europe-west1.firebasedatabase.app/")
            .build()
    }

    @Provides
    @Singleton
    fun providesContactRepository(
        dataSource: UserRequestDataSource,
        remoteUserRequestDataSource: RemoteUserRequestDataSource,
        remoteUserDataSource: RemoteUserDataSource,
        remoteImageDataSource: RemoteImageDataSource
    ): UserRequestRepository {
        return UserRequestRepositoryImpl(dataSource,remoteUserRequestDataSource,remoteUserDataSource,remoteImageDataSource)
    }

    @Provides
    @Singleton
    fun providesGetAllUserRequestsUseCase(
        repository: UserRequestRepository
    ): GetAllUserRequestsUseCase {
        return GetAllUserRequestsUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun providesGetAllCurrentUserRequestsUseCase(
        repository: UserRequestRepository
    ): GetAllCurrentUserRequestsUseCase {
        return GetAllCurrentUserRequestsUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun providesCreateContactUseCase(
        repository: UserRequestRepository
    ): CreateUserRequestUseCase {
        return CreateUserRequestUseCaseImpl(repository)
    }
    @Provides
    @Singleton
    fun providesUpdateUserRequestUseCase(
        repository: UserRequestRepository
    ): UpdateUserRequestUseCase {
        return UpdateUserRequestUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun providesGetUserRequestUseCase(
        repository: UserRequestRepository
    ): GetUserRequestUseCase {
        return GetUserRequestUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun providesGetRemoteUserRequestUseCase(
        repository: UserRequestRepository
    ): GetRemoteUserRequestUseCase {
        return GetRemoteUserRequestUseCaseImpl(repository)
    }


    @Provides
    @Singleton
    fun providesGetUserByIdRequestUseCase(
        repository: UserRequestRepository
    ): GetUserRequestByIdUseCase {
        return GetUserRequestByIdUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun providesDeleteUserRequestUseCase(
        repository: UserRequestRepository
    ): DeleteUserRequestUseCase {
        return DeleteUserRequestUseCaseImpl(repository)
    }




    //This is not a good way of instancing each datasource since we are creating multiple instances of the same database
    @Provides
    @Singleton
    fun providesUsersDatasource(
        @ApplicationContext
        context: Context
    ): UsersDataSource {
        return RoomUserDataSource(
            dao = Room.databaseBuilder(
                context,
                CityGoDatabase::class.java,
                CityGoDatabase.DATABASE_NAME
            ).build().userDao
        )
    }

    @Provides
    @Singleton
    fun providesUserProfileRepository(
        dataSource: UsersDataSource,
        remoteUserDataSource: RemoteUserDataSource,
        imageDataSource: RemoteImageDataSource
    ): UserProfileRepository {
        return UserProfileRepositoryImpl(dataSource,remoteUserDataSource,imageDataSource)
    }

    @Provides
    @Singleton
    fun providesGetUserProfileUseCase(
        repository: UserProfileRepository
    ): GetUserProfileUseCase {
        return GetUserProfileUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun providesCreateUserProfileUseCase(
        repository: UserProfileRepository
    ): CreateUserProfileUseCase {
        return CreateUserProfileUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun sendVerificationCodeUseCase(
        repository: UserProfileRepository
    ): SendVerificationCodeUseCase {
        return SendVerificationCodeUseCaseImpl(repository)
    }



    @Provides
    @Singleton
    fun updateUserProfileUseCase(
        repository: UserProfileRepository
    ): UpdateUserProfileUseCase {
        return UpdateUserProfileUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun checkIfUserProfileExistUseCase(
        repository: UserProfileRepository
    ): CheckIfUserProfileExistUseCase {
        return CheckIfUserProfileExistUseCaseImpl(repository)
    }




    @Provides
    @Singleton
    fun getUserIdUseCase(
        repository: DataStoreRepository
    ): GetUserIdUseCase {
        return GetUserIdUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun setUserIdUseCase(
        repository: DataStoreRepository
    ): SetUserIdUseCase {
        return SetUserIdUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun getUserRoleUseCase(
        repository: DataStoreRepository
    ): GetUserRoleUseCase {
        return GetUserRoleUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun setUserRoleUseCase(
        repository: DataStoreRepository
    ): SetUserRoleUseCase {
        return SetUserRoleUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun clearUserIdUseCase(
        repository: DataStoreRepository
    ): ClearUserIdUseCase {
        return ClearUserIdUseCaseImpl(repository)
    }





    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()


    @Provides
    @Singleton // Use appropriate scope
    fun provideDataStoreRepository(dataStore: DataStore<Preferences>): DataStoreRepository {
        return DataStoreRepositoryImpl(dataStore)
    }



    //--------------------------------------------------------------------------------------------


    @Provides
    @Singleton
    fun providesServiceProvidersDatasource(
        @ApplicationContext
        context: Context
    ): ServiceProvidersDataSource {
        return RoomServiceProviderDataSource(
            dao = Room.databaseBuilder(
                context,
                CityGoDatabase::class.java,
                CityGoDatabase.DATABASE_NAME
            ).build().serviceProviderDao
        )
    }

    @Provides
    @Singleton
    fun providesServiceProviderProfileRepository(
        dataSource: ServiceProvidersDataSource,
        remoteProviderDataSource: RemoteProviderDataSource,
        remoteImageDataSource: RemoteImageDataSource
    ): ServiceProviderProfileRepository {
        return ServiceProviderProfileRepositoryImpl(dataSource,remoteProviderDataSource,remoteImageDataSource)
    }
    @Provides
    @Singleton
    fun providesCreateServiceProviderProfileUseCase(
        repository: ServiceProviderProfileRepository
    ): CreateServiceProviderProfileUseCase {
        return CreateServiceProviderProfileUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun providesUpdateServiceProviderStatusUseCase(
        repository: ServiceProviderProfileRepository
    ): UpdateServiceProviderStatusUseCase {
        return UpdateServiceProviderStatusUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun providesGetServiceProviderStatusUseCase(
        repository: ServiceProviderProfileRepository
    ): GetServiceProviderProfileUseCase {
        return GetServiceProviderProfileUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun providesDeleteServiceProviderProfileUseCase(
        repository: ServiceProviderProfileRepository
    ): DeleteServiceProviderProfileUseCase {
        return DeleteServiceProviderProfileUseCaseImpl(repository)
    }



    @Provides
    @Singleton
    fun providesOfferDatasource(
        @ApplicationContext
        context: Context
    ): OfferDataSource {
        return RoomOfferDataSource(
            dao = Room.databaseBuilder(
                context,
                CityGoDatabase::class.java,
                CityGoDatabase.DATABASE_NAME
            ).build().offerDao
        )
    }

    @Provides
    @Singleton
    fun providesOfferRepository(
        dataSource: OfferDataSource,
        remoteOfferDataSource: RemoteOfferDataSource,
        remoteProviderDataSource: RemoteProviderDataSource,
        remoteUserRequestDataSource: RemoteUserRequestDataSource
    ): OfferRepository {
        return OfferRepositoryImpl(dataSource,
            remoteProviderDataSource,remoteUserRequestDataSource,remoteOfferDataSource
            )
    }
    @Provides
    @Singleton
    fun providesCreateOfferUseCase(
        repository: OfferRepository
    ): CreateOfferUseCase {
        return CreateOfferUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun providesCheckCreatedOffersUseCase(
        repository: OfferRepository
    ): CheckCreatedOffersUseCase {
        return CheckCreatedOffersUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun providesGetOfferUseCase(
        repository: OfferRepository
    ): GetOfferUseCase {
        return GetOfferUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun providesGetOfferByUserRequestUseCase(
        repository: OfferRepository
    ): GetOfferByUserRequestUseCase {
        return GetOfferByUserRequestUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun providesDeleteOfferUseCase(
        repository: OfferRepository
    ): DeleteOfferUseCase {
        return DeleteOfferUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun providesUpdateOfferStatusUseCase(
        repository: OfferRepository
    ): UpdateOfferStatusUseCase {
        return UpdateOfferStatusUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun providesHasOfferUseCase(
        repository: OfferRepository
    ): HasOfferUseCase {
        return HasOfferUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun providesGetAllOffersUseCase(
        repository: OfferRepository
    ): GetAllOffersUseCase {
        return GetAllOffersUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun providesGetAllMyOffersUseCase(
        repository: OfferRepository
    ): GetAllMyOffersUseCase {
        return GetAllMyOffersUseCaseImpl(repository)
    }





}