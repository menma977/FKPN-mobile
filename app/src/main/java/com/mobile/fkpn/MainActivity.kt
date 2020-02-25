package com.mobile.fkpn

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mobile.fkpn.content.HomeActivity
import com.mobile.fkpn.controller.VerificationController
import com.mobile.fkpn.model.Token
import org.json.JSONObject
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

    private lateinit var token: Token
    private lateinit var response: JSONObject
    private lateinit var goTo: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        token = Token(this)
    }

    override fun onStart() {
        super.onStart()
        auth()
    }

    private fun auth() {
        Timer().schedule(1000) {
            try {
                if (token.auth.isNotEmpty()) {
                    if (token.username.isNotEmpty()) {
                        response = VerificationController.Get(token.auth).execute().get()
                        when {
                            response["code"] == 200 -> {
                                runOnUiThread {
                                    if (response["data"].toString().toBoolean()) {
                                        goTo = Intent(applicationContext, HomeActivity::class.java)
                                        finish()
                                        startActivity(goTo)
                                    } else {
                                        Toast.makeText(
                                            applicationContext,
                                            "Sessi login anda sudah habis",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        goTo = Intent(applicationContext, TokenActivity::class.java)
                                        finish()
                                        startActivity(goTo)
                                    }
                                }
                            }
                            response["code"] == 500 -> {
                                runOnUiThread {
                                    goTo = Intent(applicationContext, TokenActivity::class.java)
                                    finish()
                                    startActivity(goTo)
                                }
                            }
                            else -> {
                                runOnUiThread {
                                    goTo = Intent(applicationContext, LoginActivity::class.java)
                                    finish()
                                    startActivity(goTo)
                                }
                            }
                        }
                    } else {
                        runOnUiThread {
                            goTo = Intent(applicationContext, LoginActivity::class.java)
                            finish()
                            startActivity(goTo)
                        }
                    }
                } else {
                    runOnUiThread {
                        goTo = Intent(applicationContext, LoginActivity::class.java)
                        finish()
                        startActivity(goTo)
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    goTo = Intent(applicationContext, LoginActivity::class.java)
                    finish()
                    startActivity(goTo)
                }
            }
        }
    }
}
