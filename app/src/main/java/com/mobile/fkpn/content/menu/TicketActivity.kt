package com.mobile.fkpn.content.menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.mobile.fkpn.MainActivity
import com.mobile.fkpn.R
import com.mobile.fkpn.controller.TicketController
import com.mobile.fkpn.model.Loading
import com.mobile.fkpn.model.Token
import org.json.JSONObject
import java.util.*
import kotlin.concurrent.schedule

class TicketActivity : AppCompatActivity() {

    private lateinit var token: Token
    private lateinit var loading: Loading
    private lateinit var response: JSONObject
    private lateinit var goTo: Intent
    private lateinit var username: EditText
    private lateinit var ticket: EditText
    private lateinit var ticketButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket)

        username = findViewById(R.id.usernameEditText)
        ticket = findViewById(R.id.ticketEditText)
        ticketButton = findViewById(R.id.ticketButton)

        token = Token(this)
        loading = Loading(this)

        ticketButton.setOnClickListener {
            loading.openDialog()
            Timer().schedule(100) {
                val body = HashMap<String, String>()
                body["username"] = username.text.toString()
                body["count"] = ticket.text.toString()
                response = TicketController.Post(body, token.auth).execute().get()
                when {
                    response["code"] == 200 -> {
                        runOnUiThread {
                            println(response)
                            loading.closeDialog()
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
                            Toast.makeText(
                                applicationContext,
                                response["data"].toString(),
                                Toast.LENGTH_LONG
                            ).show()
                            loading.closeDialog()
                        }
                    }
                }
            }
        }
    }
}
