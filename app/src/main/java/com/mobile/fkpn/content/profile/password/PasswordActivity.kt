package com.mobile.fkpn.content.profile.password

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mobile.fkpn.MainActivity
import com.mobile.fkpn.R
import com.mobile.fkpn.controller.PasswordController
import com.mobile.fkpn.model.Loading
import com.mobile.fkpn.model.Token
import org.json.JSONObject
import java.util.*
import kotlin.concurrent.schedule

class PasswordActivity : AppCompatActivity() {

    private lateinit var token: Token
    private lateinit var loading: Loading
    private lateinit var response: JSONObject
    private lateinit var oldPassword: EditText
    private lateinit var password: EditText
    private lateinit var passwordC: EditText
    private lateinit var save: Button
    private lateinit var goTo: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        oldPassword = findViewById(R.id.oldPasswordEditText)
        password = findViewById(R.id.newPasswordEditText)
        passwordC = findViewById(R.id.newPasswordCEditText)
        save = findViewById(R.id.saveButton)

        token = Token(this)
        loading = Loading(this)

        save.setOnClickListener {
            loading.openDialog()
            Timer().schedule(100) {
                val body = HashMap<String, String>()
                body["password"] = oldPassword.text.toString()
                body["new_password"] = password.text.toString()
                body["new_c_password"] = passwordC.text.toString()
                response = PasswordController.Post.Password(body, token.auth).execute().get()
                println(response)
                when {
                    response["code"] == 200 -> {
                        runOnUiThread {
                            loading.closeDialog()
                            Toast.makeText(
                                applicationContext,
                                response["data"].toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    response["code"] == 401 -> {
                        runOnUiThread {
                            loading.closeDialog()
                            goTo = Intent(applicationContext, MainActivity::class.java)
                            startActivity(goTo)
                            finish()
                        }
                    }
                    else -> {
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

    override fun onPause() {
        super.onPause()
        finish()
    }
}
