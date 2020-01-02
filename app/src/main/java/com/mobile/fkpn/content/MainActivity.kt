package com.mobile.fkpn.content

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.mobile.fkpn.R
import com.mobile.fkpn.TokenActivity
import com.mobile.fkpn.content.menu.BinaryActivity
import com.mobile.fkpn.content.menu.PackageJoinActivity
import com.mobile.fkpn.content.menu.WithdrawActivity
import com.mobile.fkpn.controller.BalanceController
import com.mobile.fkpn.model.Loading
import com.mobile.fkpn.model.Token
import org.json.JSONObject
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

    private lateinit var token: Token
    private lateinit var loading: Loading
    private lateinit var ticket: TextView
    private lateinit var vocerPoint: TextView
    private lateinit var bonus: TextView
    private lateinit var packageJoin: Button
    private lateinit var binary: Button
    private lateinit var withdraw: Button
    private lateinit var response: JSONObject
    private lateinit var goTo: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ticket = findViewById(R.id.ticketTextView)
        vocerPoint = findViewById(R.id.vocerPointTextView)
        bonus = findViewById(R.id.bonusTextView)
        packageJoin = findViewById(R.id.packageJoinButton)
        binary = findViewById(R.id.binaryButton)
        withdraw = findViewById(R.id.withdrawButton)

        token = Token(this)
        loading = Loading(this)

        packageJoin.setOnClickListener {
            goTo = Intent(this, PackageJoinActivity::class.java)
            startActivity(goTo)
        }

        binary.setOnClickListener {
            goTo = Intent(this, BinaryActivity::class.java)
            startActivity(goTo)
        }

        withdraw.setOnClickListener {
            goTo = Intent(this, WithdrawActivity::class.java)
            startActivity(goTo)
        }
    }

    override fun onStart() {
        super.onStart()
        setBalance()
    }

    private fun setBalance() {
        loading.openDialog()
        Timer().schedule(1000, 1000) {
            response = BalanceController(token.auth).execute().get()
            if (response["code"] == 200) {
                runOnUiThread {
                    ticket.text = response["ticket"].toString()
                    vocerPoint.text = response["vocerPoint"].toString()
                    bonus.text = response["bonus"].toString()
                    loading.closeDialog()
                    this.cancel()
                }
            } else if (response["code"] == 426) {
                runOnUiThread {
                    goTo = Intent(applicationContext, TokenActivity::class.java)
                    loading.closeDialog()
                    finish()
                    this.cancel()
                    startActivity(goTo)
                }
            }
        }
    }
}
