package com.mobile.fkpn.content.menu

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.mobile.fkpn.MainActivity
import com.mobile.fkpn.R
import com.mobile.fkpn.controller.RequestDepositController
import com.mobile.fkpn.controller.ShowDepositController
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
    private lateinit var description: TextView
    private lateinit var bank: TextView
    private lateinit var name: TextView
    private lateinit var bankAccount: TextView
    private lateinit var nominal: TextView
    private lateinit var depositButton: Button
    private lateinit var copyToClipboard: Button
    private lateinit var cardRequestDeposit: CardView

    override fun onStart() {
        super.onStart()
        showDeposit()
    }

    override fun onPause() {
        super.onPause()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deposit)

        deposit = findViewById(R.id.depositEditText)
        description = findViewById(R.id.descriptionTextView)
        bank = findViewById(R.id.bankTextView)
        name = findViewById(R.id.nameTextView)
        bankAccount = findViewById(R.id.bankAccountTextView)
        nominal = findViewById(R.id.nominalTextView)
        depositButton = findViewById(R.id.depositButton)
        cardRequestDeposit = findViewById(R.id.cardRequestDeposit)
        copyToClipboard = findViewById(R.id.copyToClipboardButton)

        token = Token(this)
        loading = Loading(this)

        depositButton.setOnClickListener {
            loading.openDialog()
            Timer().schedule(100) {
                val body = HashMap<String, String>()
                body["deposit"] = deposit.text.toString()
                response = RequestDepositController(token.auth, body).execute().get()
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

        copyToClipboard.setOnClickListener {
            val clipboard: ClipboardManager =
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip =
                ClipData.newPlainText("Nomor Rekening", bankAccount.text.toString())
            Toast.makeText(this, "Nomor Rekening telah tercopy", Toast.LENGTH_SHORT).show()
            clipboard.primaryClip = clip
        }
    }

    private fun showDeposit() {
        Timer().schedule(100) {
            response = ShowDepositController(token.auth).execute().get()
            when {
                response["code"] == 200 -> {
                    runOnUiThread {
                        cardRequestDeposit.visibility = View.VISIBLE
                        val descried = "Hallo ${
                        response.getJSONObject("data")["userName"]
                        } anda memiliki tagihan sebesar: ${
                        response.getJSONObject("data")["package"]
                        }, segera transfer dan tunggu konfirmasi oleh Admin"

                        description.text = descried
                        bank.text = response.getJSONObject("data")["name"].toString()
                        name.text = response.getJSONObject("data")["bank"].toString()
                        nominal.text = response.getJSONObject("data")["package"].toString()
                        bankAccount.text =
                            response.getJSONObject("data")["accountNumber"].toString()
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
                response["code"] == 201 -> {
                    runOnUiThread {
                        cardRequestDeposit.visibility = View.GONE
                    }
                }
                else -> {
                    runOnUiThread {
                        Toast.makeText(
                            applicationContext,
                            response["data"].toString(),
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    }
                }
            }
        }
    }
}
