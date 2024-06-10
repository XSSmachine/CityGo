package com.hfad.model

sealed class ViewState<out T : Any>
class Triumph<out T : Any>(val data: T) : ViewState<T>()
class Error<out T : Any>(val err: BasicError, val  type:String? = null) : ViewState<T>()
class Loading<out T : Any>(val text1: String? = null, val text2: String? = null, val msg: String? = null, val headImg: Int? = null, val cardImg: Int? = null) : ViewState<T>()
class NoInternetState<T : Any> : ViewState<T>()