package com.example.citygo

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.example.domain.interfaces.DataStoreRepository
import com.example.domain.interfaces.UserProfileRepository
import com.example.domain.interfaces.UserRequestRepository
import com.example.domain.interfaces.userprofile_usecases.CheckIfUserProfileExistUseCase
import com.example.domain.interfaces.userprofile_usecases.CreateUserProfileUseCase
import com.example.domain.interfaces.userprofile_usecases.GetUserIdUseCase
import com.example.domain.interfaces.userprofile_usecases.GetUserProfileUseCase
import com.example.domain.interfaces.userprofile_usecases.ReadUserIdUseCase
import com.example.domain.interfaces.userprofile_usecases.SendVerificationCodeUseCase
import com.example.domain.interfaces.userprofile_usecases.UpdateUserProfileUseCase
import com.example.domain.interfaces.userrequest_usecases.CreateUserRequestUseCase
import com.example.domain.interfaces.userrequest_usecases.GetAllUserRequestsUseCase
import com.example.domain.repositories.DataStoreRepositoryImpl
import com.example.domain.repositories.UserProfileRepositoryImpl
import com.example.domain.repositories.UserRequestRepositoryImpl
import com.example.domain.usecases.userprofile.CheckIfUserProfileExistUseCaseImpl
import com.example.domain.usecases.userprofile.CreateUserProfileUseCaseImpl
import com.example.domain.usecases.userprofile.GetUserIdUseCaseImpl
import com.example.domain.usecases.userprofile.GetUserProfileUseCaseImpl
import com.example.domain.usecases.userprofile.ReadUserIdUseCaseImpl
import com.example.domain.usecases.userprofile.SendVerificationCodeUseCaseImpl
import com.example.domain.usecases.userprofile.UpdateUserProfileUseCaseImpl
import com.example.domain.usecases.userrequest.CreateUserRequestUseCaseImpl
import com.example.domain.usecases.userrequest.GetAllUserRequestsUseCaseImpl
import com.example.repository.datasources.room.RoomUserDataSource
import com.example.repository.datasources.room.RoomUserRequestDataSource
import com.example.repository.interfaces.UserRequestDataSource
import com.example.repository.interfaces.CityGoDatabase
import com.example.repository.interfaces.UsersDataSource
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton


/**
 * This is Dagger module which is responsible for providing various dependencies
 * required by my application using Dagger's DI framework.
 * @author Karlo Kovačević
 */

private const val USER_PREFERENCES_NAME = "user_preferences"
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
    @Singleton
    fun providesContactRepository(
        dataSource: UserRequestDataSource
    ): UserRequestRepository {
        return UserRequestRepositoryImpl(dataSource)
    }

    @Provides
    @Singleton
    fun providesGetContactsUseCase(
        repository: UserRequestRepository
    ): GetAllUserRequestsUseCase {
        return GetAllUserRequestsUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun providesCreateContactUseCase(
        repository: UserRequestRepository
    ): CreateUserRequestUseCase {
        return CreateUserRequestUseCaseImpl(repository)
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
        dataSource: UsersDataSource
    ): UserProfileRepository {
        return UserProfileRepositoryImpl(dataSource)
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
    fun providesUserDataStoreRepository(
        dataSource: DataStore<Preferences>,
        @ApplicationContext
        context: Context
    ): DataStoreRepository {
        return DataStoreRepositoryImpl(dataSource,context)
    }
    @Provides
    @Singleton
    fun readUserIdUseCase(
        repository: DataStoreRepository
    ): ReadUserIdUseCase {
        return ReadUserIdUseCaseImpl(repository)
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
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providePreferencesDataStore(@ApplicationContext appContext: Context) : DataStore<androidx.datastore.preferences.core.Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = {emptyPreferences()}
            ),
            migrations = listOf(SharedPreferencesMigration(appContext, USER_PREFERENCES_NAME)),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile={appContext.preferencesDataStoreFile(USER_PREFERENCES_NAME)}
        )
    }

}