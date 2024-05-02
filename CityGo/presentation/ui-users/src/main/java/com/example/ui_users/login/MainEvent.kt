package com.example.ui_users.login

sealed class MainEvent {
    object NamedCachedSuccess : MainEvent()
    class CachedNameFetchSuccess(
        val userId: String
    ) : MainEvent()
}