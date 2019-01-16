package com.marceme.cashbackexplorer.login

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import com.marceme.cashbackexplorer.R
import com.marceme.cashbackexplorer.hideKeyboard
import com.marceme.cashbackexplorer.network.APIServiceFactory
import kotlinx.android.synthetic.main.activity_login.*


const val PREF_FILE_NAME = "com.marceme.cashbackexplorer.preference"
const val PREF_KEY_TOKEN = "com.marceme.cashbackexplorer.access_token"
class LoginActivity : AppCompatActivity(), LoginView {

    private lateinit var presenterLogin: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        presenterLogin = LoginPresenter(this, APIServiceFactory.makeApiService())
        log_in_button.setOnClickListener { logIn() }
    }

    private fun logIn() {
        presenterLogin.logIn()
    }

    override fun clearErrors() {
        name.error = null
        email.error = null
    }

    override fun getName(): String {
        return name.text.toString()
    }

    override fun getEmail(): String {
        return email.text.toString()
    }

    override fun setNameError() {
        name.error = getString(R.string.error_field_required)
    }

    override fun setEmailError() {
        email.error = getString(R.string.error_field_required)
    }

    override fun setInvalidEmailError() {
        email.error = getString(R.string.error_invalid_email)
    }

    override fun showLoginProgress() {
        hideKeyboard()
        progress_bar.visibility = VISIBLE
        log_in_button.isEnabled = false
    }

    override fun hideLoginProgress() {
        progress_bar.visibility = GONE
        log_in_button.isEnabled = true
    }

    override fun logInError() {
        Toast.makeText(this, getString(R.string.error_login), Toast.LENGTH_LONG).show()
    }

    override fun showVenues(token: String) {
        val pref = this.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
        pref.edit().putString(PREF_KEY_TOKEN, token).apply()
        Toast.makeText(this, "Show venues", Toast.LENGTH_LONG).show()
    }

}
