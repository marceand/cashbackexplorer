package com.marceme.cashbackexplorer.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.marceme.cashbackexplorer.R
import com.marceme.cashbackexplorer.explore.ExplorerActivity
import com.marceme.cashbackexplorer.login.LoginActivity
import com.marceme.cashbackexplorer.login.PREF_FILE_NAME
import com.marceme.cashbackexplorer.login.PREF_KEY_TOKEN


class SplashActivity : AppCompatActivity() {

    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        handler = Handler()
        handler.postDelayed(updateRunnable, 1000)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateRunnable)
    }

    private val updateRunnable = object : Runnable {
        override fun run() {
            if(getToken().isEmpty()){
                showLogin()
            }else{
                showExplorer()
            }
        }
    }

    private fun showExplorer() {
        val intent = Intent(this, ExplorerActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun getToken(): String {
        val preference = this.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
        return preference.getString(PREF_KEY_TOKEN, "")
    }

}
