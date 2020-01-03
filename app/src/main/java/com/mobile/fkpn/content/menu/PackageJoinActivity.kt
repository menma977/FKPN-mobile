package com.mobile.fkpn.content.menu

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.mobile.fkpn.R
import com.mobile.fkpn.controller.InvestmentController
import com.mobile.fkpn.controller.StorePackageController
import com.mobile.fkpn.model.Loading
import com.mobile.fkpn.model.Token
import org.json.JSONArray
import java.util.*
import kotlin.concurrent.schedule

class PackageJoinActivity : AppCompatActivity() {

    private lateinit var token: Token
    private lateinit var loading: Loading
    private lateinit var response: JSONArray
    private lateinit var goTo: Intent
    private lateinit var package1: Button
    private lateinit var package2: Button
    private lateinit var package3: Button
    private lateinit var package4: Button
    private lateinit var contentTable: LinearLayout

    override fun onPause() {
        super.onPause()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_package_join)

        package1 = findViewById(R.id.package1Button)
        package2 = findViewById(R.id.package2Button)
        package3 = findViewById(R.id.package3Button)
        package4 = findViewById(R.id.package4Button)
        contentTable = findViewById(R.id.tables)

        token = Token(this)
        loading = Loading(this)
        loading.openDialog()
        getListInvest()

        package1.setOnClickListener {
            loading.openDialog()
            Timer().schedule(100) {
                val res = StorePackageController(token.auth, "1").execute().get()
                if (res["code"] == 200) {
                    runOnUiThread {
                        Toast.makeText(
                            applicationContext,
                            res["data"].toString(),
                            Toast.LENGTH_LONG
                        ).show()
                        loading.closeDialog()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(
                            applicationContext,
                            res["data"].toString(),
                            Toast.LENGTH_LONG
                        ).show()
                        loading.closeDialog()
                    }
                }
            }
        }

        package2.setOnClickListener {
            loading.openDialog()
            Timer().schedule(100) {
                val res = StorePackageController(token.auth, "2").execute().get()
                if (res["code"] == 200) {
                    runOnUiThread {
                        Toast.makeText(
                            applicationContext,
                            res["data"].toString(),
                            Toast.LENGTH_LONG
                        ).show()
                        loading.closeDialog()
                        finish()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(
                            applicationContext,
                            res["data"].toString(),
                            Toast.LENGTH_LONG
                        ).show()
                        loading.closeDialog()
                    }
                }
            }
        }

        package3.setOnClickListener {
            loading.openDialog()
            Timer().schedule(100) {
                val res = StorePackageController(token.auth, "3").execute().get()
                if (res["code"] == 200) {
                    runOnUiThread {
                        Toast.makeText(
                            applicationContext,
                            res["data"].toString(),
                            Toast.LENGTH_LONG
                        ).show()
                        loading.closeDialog()
                        finish()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(
                            applicationContext,
                            res["data"].toString(),
                            Toast.LENGTH_LONG
                        ).show()
                        loading.closeDialog()
                    }
                }
            }
        }

        package4.setOnClickListener {
            loading.openDialog()
            Timer().schedule(100) {
                val res = StorePackageController(token.auth, "4").execute().get()
                if (res["code"] == 200) {
                    runOnUiThread {
                        Toast.makeText(
                            applicationContext,
                            res["data"].toString(),
                            Toast.LENGTH_LONG
                        ).show()
                        loading.closeDialog()
                        finish()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(
                            applicationContext,
                            res["data"].toString(),
                            Toast.LENGTH_LONG
                        ).show()
                        loading.closeDialog()
                    }
                }
            }
        }
    }

    private fun getListInvest() {
        response = InvestmentController(token.auth).execute().get()
        if (response[0] == 200) {
            val dataResponse = response.getJSONObject(1)
            if (dataResponse["status"] == 0) {
                package1.isEnabled = false
                package2.isEnabled = false
                package3.isEnabled = false
                package4.isEnabled = false
            } else {
                when {
                    dataResponse["join"] == 500000 -> {
                        package1.isEnabled = true
                        package2.isEnabled = true
                        package3.isEnabled = true
                        package4.isEnabled = true
                    }
                    dataResponse["join"] == 1000000 -> {
                        package1.isEnabled = false
                        package2.isEnabled = true
                        package3.isEnabled = true
                        package4.isEnabled = true
                    }
                    dataResponse["join"] == 5000000 -> {
                        package1.isEnabled = false
                        package2.isEnabled = true
                        package3.isEnabled = false
                        package4.isEnabled = true
                    }
                    else -> {
                        package1.isEnabled = false
                        package2.isEnabled = false
                        package3.isEnabled = false
                        package4.isEnabled = true
                    }
                }
            }
            generateTable()
        } else {
            package1.isEnabled = false
            package2.isEnabled = false
            package3.isEnabled = false
            package4.isEnabled = false
            loading.closeDialog()
        }
    }

    private fun generateTable() {
        contentTable.removeAllViews()

        val optionTableBody = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val optionRow = LinearLayout.LayoutParams(
            280,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        optionRow.gravity = Gravity.CENTER

        val contentLinear = LinearLayout(this)
        contentLinear.orientation = LinearLayout.HORIZONTAL
        contentLinear.layoutParams = optionTableBody

        val headerTextView1 = TextView(this)
        headerTextView1.gravity = Gravity.CENTER
        headerTextView1.text = "Reinvest Ke"
        headerTextView1.layoutParams = optionRow
        contentLinear.addView(headerTextView1)

        val headerTextView2 = TextView(this)
        headerTextView2.gravity = Gravity.CENTER
        headerTextView2.text = "Join"
        headerTextView2.layoutParams = optionRow
        contentLinear.addView(headerTextView2)

        val headerTextView3 = TextView(this)
        headerTextView3.gravity = Gravity.CENTER
        headerTextView3.text = "Package"
        headerTextView3.layoutParams = optionRow
        contentLinear.addView(headerTextView3)

        val headerTextView4 = TextView(this)
        headerTextView4.gravity = Gravity.CENTER
        headerTextView4.text = "Profit"
        headerTextView4.layoutParams = optionRow
        contentLinear.addView(headerTextView4)

        val headerTextView5 = TextView(this)
        headerTextView5.gravity = Gravity.CENTER
        headerTextView5.text = "Status"
        headerTextView5.layoutParams = optionRow
        contentLinear.addView(headerTextView5)

        contentTable.addView(contentLinear)

        if (response.getJSONArray(2).length() != 0) {
            for (value in 0 until response.getJSONArray(2).length()) {
                val dataResponse = response.getJSONArray(2).getJSONObject(value)

                val bodyContentLinear = LinearLayout(this)
                bodyContentLinear.orientation = LinearLayout.HORIZONTAL
                bodyContentLinear.layoutParams = optionTableBody

                val textView1 = TextView(this)
                textView1.gravity = Gravity.CENTER
                textView1.text = dataResponse["reinvest"].toString()
                textView1.layoutParams = optionRow
                bodyContentLinear.addView(textView1)

                val textView2 = TextView(this)
                textView2.gravity = Gravity.CENTER
                textView2.text = dataResponse["join"].toString()
                textView2.layoutParams = optionRow
                bodyContentLinear.addView(textView2)

                val textView3 = TextView(this)
                textView3.gravity = Gravity.CENTER
                textView3.text = dataResponse["package"].toString()
                textView3.layoutParams = optionRow
                bodyContentLinear.addView(textView3)

                val textView4 = TextView(this)
                textView4.gravity = Gravity.CENTER
                textView4.text = dataResponse["profit"].toString()
                textView4.layoutParams = optionRow
                bodyContentLinear.addView(textView4)

                val textView5 = TextView(this)
                textView5.gravity = Gravity.CENTER
                if (dataResponse["status"] == 2) {
                    textView5.text = "Tervalidasi"
                } else {
                    textView5.text = "Lunas"
                }
                textView5.layoutParams = optionRow
                bodyContentLinear.addView(textView5)

                contentTable.addView(bodyContentLinear)
            }
        }
        loading.closeDialog()
    }
}
