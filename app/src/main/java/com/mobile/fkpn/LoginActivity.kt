package com.mobile.fkpn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.mobile.fkpn.content.HomeActivity
import com.mobile.fkpn.controller.LoginController
import com.mobile.fkpn.controller.VerificationController
import com.mobile.fkpn.model.Loading
import com.mobile.fkpn.model.Token
import org.json.JSONObject
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap
import kotlin.concurrent.schedule

class LoginActivity : AppCompatActivity() {

    private lateinit var token: Token
    private lateinit var loading: Loading
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var login: Button
    private lateinit var response: JSONObject
    private lateinit var goTo: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        username = findViewById(R.id.usernameTextView)
        password = findViewById(R.id.passwordTextView)
        login = findViewById(R.id.loginButton)

        token = Token(this)
        loading = Loading(this)

        login.setOnClickListener {
            loading.openDialog()
            Timer().schedule(100) {
                val parameter = HashMap<String, String>()
                parameter["username"] = username.text.toString()
                parameter["password"] = password.text.toString()
                response = LoginController(parameter).execute().get()
                if (response["code"] == 200) {
                    val json = JSONObject()
                    json.put("username", parameter["username"])
                    json.put("auth", response["data"].toString())
                    token.set(json.toString())
                    runOnUiThread {
                        Timer().schedule(100) {
                            runOnUiThread {
                                goTo = Intent(applicationContext, HomeActivity::class.java)
                                loading.closeDialog()
                                finish()
                                startActivity(goTo)
                            }
                        }
                    }
                } else {
                    runOnUiThread {
                        loading.closeDialog()
                        Toast.makeText(
                            applicationContext,
                            response["data"].toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}
