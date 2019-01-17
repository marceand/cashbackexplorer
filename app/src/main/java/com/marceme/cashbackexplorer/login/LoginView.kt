package com.marceme.cashbackexplorer.login

interface LoginView {
    fun clearErrors()

    fun getName(): String

    fun getEmail(): String

    fun setNameError()

    fun setEmailError()

    fun setInvalidEmailError()

    fun showLoginProgress()

    fun hideLoginProgress()

    fun showLogInError()

    fun saveToken(token: String)

    fun showVenues()
}