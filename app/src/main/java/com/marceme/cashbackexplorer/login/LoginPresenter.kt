package com.marceme.cashbackexplorer.login

import com.marceme.cashbackexplorer.isValidEmail
import com.marceme.cashbackexplorer.model.User
import com.marceme.cashbackexplorer.network.APIService
import com.marceme.cashbackexplorer.model.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginPresenter(private val loginView: LoginView, private val apiService: APIService) {


    fun logIn() {
        loginView.clearErrors()

        if(loginView.getName().isEmpty()){
            loginView.setNameError()
            return
        }

        if(loginView.getEmail().isEmpty()){
            loginView.setEmailError()
            return
        }else if (!loginView.getEmail().isValidEmail()){
            loginView.setInvalidEmailError()
            return
        }


        loginView.showLoginProgress()

        val user = User(loginView.getName(), loginView.getEmail())
        apiService.createUser(user).enqueue(object : Callback<UserResponse> {

            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                loginView.hideLoginProgress()
                if(response.isSuccessful){
                    val token = response.headers()["token"]?:""
                    loginView.saveToken(token)
                    loginView.showVenues()
                }else{
                    loginView.showLogInError()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                loginView.hideLoginProgress()
                loginView.showLogInError()
            }
        })
    }
}