package com.mobile.fkpn.content.menu.vocerPoint

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import com.mobile.fkpn.MainActivity
import com.mobile.fkpn.R
import com.mobile.fkpn.controller.VocerPointListController
import com.mobile.fkpn.model.Loading
import com.mobile.fkpn.model.Token
import org.json.JSONObject
import java.util.*
import kotlin.concurrent.schedule

class VocerPointListActivity : AppCompatActivity() {

    private lateinit var token: Token
    private lateinit var loading: Loading
    private lateinit var response: JSONObject
    private lateinit var goTo: Intent
    private lateinit var contentTable: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vocer_point_list)

        contentTable = findViewById(R.id.tables)
        token = Token(this)
        loading = Loading(this)
    }

    override fun onPause() {
        super.onPause()
        finish()
    }

    override fun onStart() {
        super.onStart()
        generateTable()
    }

    private fun generateTable() {
        Timer().schedule(100) {
            runOnUiThread {
                response = VocerPointListController.Get(token.auth).execute().get()
                if (response["code"] == 401) {
                    runOnUiThread {
                        loading.closeDialog()
                        goTo = Intent(applicationContext, MainActivity::class.java)
                        startActivity(goTo)
                        finish()
                    }
                }
                contentTable.removeAllViews()
                val optionTableBody = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )

                val optionRow = LinearLayout.LayoutParams(
                    400,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                optionRow.gravity = Gravity.CENTER

                val contentLinear = LinearLayout(applicationContext)
                contentLinear.orientation = LinearLayout.HORIZONTAL
                contentLinear.layoutParams = optionTableBody

                val headerTextView1 = TextView(applicationContext)
                headerTextView1.gravity = Gravity.CENTER
                headerTextView1.text = "Description"
                headerTextView1.layoutParams = optionRow
                headerTextView1.minWidth = 500
                headerTextView1.width = 500
                contentLinear.addView(headerTextView1)

                val headerTextView2 = TextView(applicationContext)
                headerTextView2.gravity = Gravity.CENTER
                headerTextView2.text = "Debit"
                headerTextView2.layoutParams = optionRow
                headerTextView2.minWidth = 100
                headerTextView2.width = 100
                contentLinear.addView(headerTextView2)

                val headerTextView3 = TextView(applicationContext)
                headerTextView3.gravity = Gravity.CENTER
                headerTextView3.text = "Credit"
                headerTextView3.layoutParams = optionRow
                headerTextView3.minWidth = 100
                headerTextView3.width = 100
                contentLinear.addView(headerTextView3)

                val headerTextView4 = TextView(applicationContext)
                headerTextView4.gravity = Gravity.CENTER
                headerTextView4.text = "Tanggal"
                headerTextView4.layoutParams = optionRow
                headerTextView4.minWidth = 200
                headerTextView4.width = 200
                contentLinear.addView(headerTextView4)

                contentTable.addView(contentLinear)

                if (response.getJSONArray("data").length() != 0) {
                    for (value in 0 until response.getJSONArray("data").length()) {
                        val dataResponse = response.getJSONArray("data").getJSONObject(value)

                        val bodyContentLinear = LinearLayout(applicationContext)
                        bodyContentLinear.orientation = LinearLayout.HORIZONTAL
                        bodyContentLinear.layoutParams = optionTableBody

                        val textView1 = TextView(applicationContext)
                        textView1.gravity = Gravity.CENTER
                        textView1.text = dataResponse["description"].toString()
                        textView1.layoutParams = optionRow
                        textView1.minWidth = 500
                        textView1.width = 500
                        bodyContentLinear.addView(textView1)

                        val textView2 = TextView(applicationContext)
                        textView2.gravity = Gravity.CENTER
                        textView2.text = dataResponse["debit"].toString()
                        textView2.layoutParams = optionRow
                        textView2.minWidth = 100
                        textView2.width = 100
                        bodyContentLinear.addView(textView2)

                        val textView3 = TextView(applicationContext)
                        textView3.gravity = Gravity.CENTER
                        textView3.text = dataResponse["credit"].toString()
                        textView3.layoutParams = optionRow
                        textView3.minWidth = 100
                        textView3.width = 100
                        bodyContentLinear.addView(textView3)

                        val textView4 = TextView(applicationContext)
                        textView4.gravity = Gravity.CENTER
                        textView4.text = dataResponse["updated_at"].toString()
                        textView4.layoutParams = optionRow
                        textView4.minWidth = 200
                        textView4.width = 200
                        bodyContentLinear.addView(textView4)

                        contentTable.addView(bodyContentLinear)
                    }
                }
                loading.closeDialog()
            }
        }
    }
}
