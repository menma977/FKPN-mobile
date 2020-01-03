package com.mobile.fkpn.content.menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.mobile.fkpn.LoginActivity
import com.mobile.fkpn.R
import com.mobile.fkpn.controller.RequestDepositController
import com.mobile.fkpn.model.Loading
import com.mobile.fkpn.model.Token
import org.json.JSONObject
import java.util.*
import kotlin.concurrent.schedule

class DepositActivity : AppCompatActivity() {

    private lateinit var token: Token
    private lateinit var loading: Loading
    private lateinit var response: JSONObject
    private lateinit var goTo: Intent
    private lateinit var deposit: TextView
    private lateinit var depositButton: Button

    override fun onPause() {
        super.onPause()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deposit)

        deposit = findViewById(R.id.depositEditText)
        depositButton = findViewById(R.id.depositButton)

        token = Token(this)
        loading = Loading(this)

        depositButton.setOnClickListener {
            loading.openDialog()
            Timer().schedule(100) {
                val body = HashMap<String, String>()
                body["deposit"] = deposit.text.toString()
                response = RequestDepositController(token.auth, body).execute().get()
                println(response)
                when {
                    response["code"] == 200 -> {
                        runOnUiThread {
                            loading.closeDialog()
                            Toast.makeText(
                                applicationContext,
                                "Deposit akan di proses oleh admin",
                                Toast.LENGTH_SHORT
                            ).show()
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
}
