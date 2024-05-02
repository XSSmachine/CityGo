package com.example.userrequest.offer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.interfaces.offer_usecases.CreateOfferUseCase
import com.example.domain.interfaces.userprofile_usecases.GetUserIdUseCase
import com.example.domain.interfaces.userrequest_usecases.CreateUserRequestUseCase
import com.hfad.model.OfferRequestModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class OfferViewModel @Inject constructor(
    private val createOfferUseCase: CreateOfferUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    ) : ViewModel() {

    suspend fun getUserId(): Result<String?> {
        return getUserIdUseCase.execute()
    }


    fun getIdValue() : String{
        var r = ""
        runBlocking {
            r = getUserId().getOrNull()?:"none"
        }
        Log.d("VIEWMODEL-offer",r)
        return r
    }



    }