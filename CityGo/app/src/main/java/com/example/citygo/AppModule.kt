package com.example.citygo

import android.content.Context
import androidx.room.Room
import com.example.domain.interfaces.UserRequestRepository
import com.example.domain.interfaces.usecases.CreateUserRequestUseCase
import com.example.domain.interfaces.usecases.GetAllUserRequestsUseCase
import com.example.domain.repositories.UserRequestRepositoryImpl
import com.example.domain.usecases.userrequest.CreateUserRequestUseCaseImpl
import com.example.domain.usecases.userrequest.GetAllUserRequestsUseCaseImpl
import com.example.repository.datasources.room.RoomUserRequestDataSource
import com.example.repository.interfaces.UserRequestDataSource
import com.example.repository.interfaces.UserRequestDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesContactDatasource(
        @ApplicationContext
        context: Context
    ): UserRequestDataSource {
        return RoomUserRequestDataSource(
            dao = Room.databaseBuilder(
                context,
                UserRequestDatabase::class.java,
                UserRequestDatabase.DATABASE_NAME
            ).build().contactDao
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
}