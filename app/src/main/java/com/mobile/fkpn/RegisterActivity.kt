package com.mobile.fkpn

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.Toast
import com.mobile.fkpn.content.HomeActivity
import com.mobile.fkpn.controller.RegisterController
import com.mobile.fkpn.model.Loading
import com.mobile.fkpn.model.Token
import org.json.JSONObject
import java.util.*
import kotlin.collections.HashMap
import kotlin.concurrent.schedule

class RegisterActivity : AppCompatActivity() {

    private lateinit var token: Token
    private lateinit var loading: Loading
    private lateinit var sponsor: EditText
    private lateinit var name: EditText
    private lateinit var username: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var validationPassword: EditText
    private lateinit var passwordX: EditText
    private lateinit var validationPasswordX: EditText
    private lateinit var ktpNumber: EditText
    private lateinit var phone: EditText
    private lateinit var province: EditText
    private lateinit var district: EditText
    private lateinit var subDistrict: EditText
    private lateinit var village: EditText
    private lateinit var numberAddress: EditText
    private lateinit var descriptionAddress: EditText
    private lateinit var position: SeekBar
    private lateinit var register: Button
    private lateinit var response: JSONObject
    private lateinit var goTo: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        sponsor = findViewById(R.id.sponsorEditText)
        name = findViewById(R.id.nameEditText)
        username = findViewById(R.id.usernameEditText)
        email = findViewById(R.id.emailEditText)
        password = findViewById(R.id.passwordEditText)
        validationPassword = findViewById(R.id.validationPasswordEditText)
        passwordX = findViewById(R.id.passwordXEditText)
        validationPasswordX = findViewById(R.id.validationPasswordXEditText)
        ktpNumber = findViewById(R.id.accountIdEditText)
        phone = findViewById(R.id.phoneEditText)
        province = findViewById(R.id.provinceEditText)
        district = findViewById(R.id.districtEditText)
        subDistrict = findViewById(R.id.subDistrictEditText)
        village = findViewById(R.id.villageEditText)
        numberAddress = findViewById(R.id.numberAddressEditText)
        descriptionAddress = findViewById(R.id.descriptionAddressEditText)
        position = findViewById(R.id.positionSeekBar)
        register = findViewById(R.id.registerButton)

        token = Token(this)
        loading = Loading(this)
        loading.openDialog()

        loading.closeDialog()
        register.setOnClickListener {
            loading.openDialog()
            Timer().schedule(100) {
                val massage: String
                val parameter = HashMap<String, String>()
                parameter["sponsor"] = sponsor.text.toString()
                parameter["name"] = name.text.toString()
                parameter["username"] = username.text.toString()
                parameter["email"] = email.text.toString()
                parameter["password"] = password.text.toString()
                parameter["c_password"] = validationPassword.text.toString()
                parameter["password_x"] = password.text.toString()
                parameter["c_password_x"] = validationPassword.text.toString()
                parameter["ktp_number"] = ktpNumber.text.toString()
                parameter["phone"] = phone.text.toString()
                parameter["province"] = province.text.toString()
                parameter["district"] = district.text.toString()
                parameter["sub_district"] = subDistrict.text.toString()
                parameter["village"] = village.text.toString()
                parameter["number_address"] = numberAddress.text.toString()
                parameter["description_address"] = descriptionAddress.text.toString()
                parameter["position"] = position.progress.toString()
                response = RegisterController(parameter).execute().get()
                if (response["code"] == 200) {
                    runOnUiThread {
                        loading.closeDialog()
                        val json = JSONObject()
                        json.put("username", parameter["username"])
                        json.put("auth", response["data"].toString())
                        token.set(json.toString())
                        loading.closeDialog()
                        goTo = Intent(applicationContext, HomeActivity::class.java)
                        startActivity(goTo)
                    }
                } else {
                    when {
                        response["name"].toString() in "sponsor" -> {
                            massage = response["data"].toString()
                                .replace(response["name"].toString(), "Afiliasi")
                        }
                        response["name"].toString() in "name" -> {
                            massage = response["data"].toString()
                                .replace(response["name"].toString(), "Nama")
                        }
                        response["name"].toString() in "username" -> {
                            massage = response["data"].toString()
                                .replace(response["name"].toString(), "Username")
                        }
                        response["name"].toString() in "email" -> {
                            massage = response["data"].toString()
                                .replace(response["name"].toString(), "Email")
                        }
                        response["name"].toString() in "password" -> {
                            massage = response["data"].toString()
                                .replace(response["name"].toString(), "Password")
                        }
                        response["name"].toString() in "c_password" -> {
                            massage = response["data"].toString()
                                .replace(response["name"].toString(), "Konfrimasi Password")
                        }
                        response["name"].toString() in "password_x" -> {
                            massage = response["data"].toString()
                                .replace(response["name"].toString(), "Password Transaksi")
                        }
                        response["name"].toString() in "c_password_x" -> {
                            massage = response["data"].toString()
                                .replace(
                                    response["name"].toString(),
                                    "Konfrimasi Password Transaksi"
                                )
                        }
                        response["name"].toString() in "ktp_number" -> {
                            massage = response["data"].toString()
                                .replace(response["name"].toString(), "Nomor KTP")
                        }
                        response["name"].toString() in "phone" -> {
                            massage = response["data"].toString()
                                .replace(response["name"].toString(), "Nomor Telfon")
                        }
                        response["name"].toString() in "province" -> {
                            massage = response["data"].toString()
                                .replace(response["name"].toString(), "Profinsi")
                        }
                        response["name"].toString() in "district" -> {
                            massage = response["data"].toString()
                                .replace(response["name"].toString(), "Kota/Kabupaten")
                        }
                        response["name"].toString() in "sub_district" -> {
                            massage = response["data"].toString()
                                .replace(response["name"].toString(), "Keluarahan")
                        }
                        response["name"].toString() in "village" -> {
                            massage = response["data"].toString()
                                .replace(response["name"].toString(), "Kel/Desa")
                        }
                        response["name"].toString() in "number_address" -> {
                            massage = response["data"].toString()
                                .replace(response["name"].toString(), "Nomor Rumah")
                        }
                        response["name"].toString() in "description_address" -> {
                            massage = response["data"].toString()
                                .replace(response["name"].toString(), "Keterangan Alamat Lengkap")
                        }
                        else -> {
                            massage = response["data"].toString()
                        }
                    }
                    runOnUiThread {
                        loading.closeDialog()
                        Toast.makeText(
                            applicationContext,
                            massage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask()
        }
    }
}
