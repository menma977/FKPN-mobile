package com.mobile.fkpn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.mobile.fkpn.content.HomeActivity
import com.mobile.fkpn.controller.LoginController
import com.mobile.fkpn.controller.LogoutController
import com.mobile.fkpn.model.Loading
import com.mobile.fkpn.model.Token
import org.json.JSONObject
import java.util.*
import kotlin.concurrent.schedule

class TokenActivity : AppCompatActivity() {

    private lateinit var token: Token
    private lateinit var loading: Loading
    private lateinit var username: TextView
    private lateinit var password: EditText
    private lateinit var login: Button
    private lateinit var response: JSONObject
    private lateinit var goTo: Intent
    private var missToken = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_token)

        username = findViewById(R.id.usernameTextView)
        password = findViewById(R.id.passwordEditText)
        login = findViewById(R.id.loginButton)
        token = Token(this)
        loading = Loading(this)
        username.text = token.username

        login.setOnClickListener {
            loading.openDialog()
            Timer().schedule(100) {
                if (token.auth.isNotEmpty()) {
                    response = LogoutController(token.auth).execute().get()
                }
                val parameter = HashMap<String, String>()
                parameter["username"] = token.username
                parameter["password"] = password.text.toString()
                response = LoginController(parameter).execute().get()
                if (response["code"] == 200) {
                    val json = JSONObject()
                    json.put("username", parameter["username"])
                    json.put("auth", response["data"].toString())
                    token.set(json.toString())
                    runOnUiThread {
                        goTo = Intent(applicationContext, HomeActivity::class.java)
                        finish()
                        loading.closeDialog()
                        startActivity(goTo)
                    }
                } else {
                    runOnUiThread {
                        loading.closeDialog()
                        if (missToken == 2) {
                            token.clear()
                            Toast.makeText(
                                applicationContext,
                                "Anda salah password lebih dari 3x",
                                Toast.LENGTH_SHORT
                            ).show()
                            goTo = Intent(applicationContext, LoginActivity::class.java)
                            finish()
                            startActivity(goTo)
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Password tidak valid",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        missToken++
                    }
                }
            }
        }
    }
}
