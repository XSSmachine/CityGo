package com.example.repository.datasources.room


import com.example.repository.datasources.room.entities.toContactResponseModel
import com.example.repository.datasources.room.entities.toContactRoomEntity
import com.example.repository.interfaces.UserRequestDao
import com.example.repository.interfaces.UserRequestDataSource
import com.hfad.model.UserRequestRequestModel
import com.hfad.model.UserRequestResponseModel

class RoomUserRequestDataSource constructor(private val dao: UserRequestDao) :
    UserRequestDataSource {
    override suspend fun getAll(): List<UserRequestResponseModel> {
        return dao.getAll().toList().map {
            it.toContactResponseModel()
        }
    }

    override suspend fun getOne(id: Int): UserRequestResponseModel? {
        val entity = dao.getById(id)
        if (entity != null) {
            return entity.toContactResponseModel()
        }
        return null
    }

    override suspend fun delete(id: Int) {
        dao.deleteById(id)
    }

    override suspend fun update(id: Int, UserRequest: UserRequestRequestModel) {
        dao.updateDescription(id, UserRequest.description)
    }

    override suspend fun create(data: UserRequestRequestModel) {
        dao.insert(data.toContactRoomEntity())
    }



}