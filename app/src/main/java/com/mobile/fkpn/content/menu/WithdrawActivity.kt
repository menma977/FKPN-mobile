package com.mobile.fkpn.content.menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.mobile.fkpn.LoginActivity
import com.mobile.fkpn.R
import com.mobile.fkpn.controller.WithdrawController
import com.mobile.fkpn.model.Loading
import com.mobile.fkpn.model.Token
import org.json.JSONObject
import java.util.*
import kotlin.concurrent.schedule

class WithdrawActivity : AppCompatActivity() {

    private lateinit var token: Token
    private lateinit var loading: Loading
    private lateinit var response: JSONObject
    private lateinit var goTo: Intent
    private lateinit var withdrawEditText: EditText
    private lateinit var withdrawButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_withdraw)

        withdrawEditText = findViewById(R.id.withdrawEditText)
        withdrawButton = findViewById(R.id.withdrawButton)

        token = Token(this)
        loading = Loading(this)

        withdrawButton.setOnClickListener {
            loading.openDialog()
            Timer().schedule(100) {
                val body = HashMap<String, String>()
                body["nominal"] = withdrawEditText.text.toString()
                response = WithdrawController(body, token.auth).execute().get()
                when {
                    response["code"] == 200 -> {
                        runOnUiThread {
                            loading.closeDialog()
                            Toast.makeText(
                                applicationContext,
                                "Withdraw akan di proses di hari Senin",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    response["code"] == 422 -> {
                        runOnUiThread {
                            loading.closeDialog()
                            Toast.makeText(
                                applicationContext,
                                response["data"].toString()
                                    .replace("nominal", "nominal withdraw"),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    else -> {
                        runOnUiThread {
                            loading.closeDialog()
                            token.clear()
                            Toast.makeText(
                                applicationContext,
                                "Sessi Login Anda Berakir",
                                Toast.LENGTH_SHORT
                            ).show()
                            goTo = Intent(applicationContext, LoginActivity::class.java)
                            finish()
                            startActivity(goTo)
                        }
                    }
                }
            }
        }
    }
}
