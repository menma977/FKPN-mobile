package com.mobile.fkpn.content.profile

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mobile.fkpn.MainActivity
import com.mobile.fkpn.R
import com.mobile.fkpn.controller.ImageGeneratorController
import com.mobile.fkpn.controller.ProfileController
import com.mobile.fkpn.controller.UpdateProfileController
import com.mobile.fkpn.controller.UploadImageController
import com.mobile.fkpn.model.Loading
import com.mobile.fkpn.model.Token
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.concurrent.schedule

class ProfileActivity : AppCompatActivity() {

    private lateinit var token: Token
    private lateinit var loading: Loading
    private lateinit var response: JSONObject
    private lateinit var goTo: Intent
    private lateinit var name: EditText
    private lateinit var province: EditText
    private lateinit var district: EditText
    private lateinit var subDistrict: EditText
    private lateinit var village: EditText
    private lateinit var descriptionAddress: EditText
    private lateinit var numberAddress: EditText
    private lateinit var profileImage: ImageView
    private lateinit var saveButton: Button
    private var image: Uri? = null
    private var filePath: String = ""
    private var fileName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        name = findViewById(R.id.nameEditText)
        province = findViewById(R.id.provinceEditText)
        district = findViewById(R.id.districtEditText)
        subDistrict = findViewById(R.id.subDistrictEditText)
        village = findViewById(R.id.villageEditText)
        descriptionAddress = findViewById(R.id.descriptionAddressEditText)
        numberAddress = findViewById(R.id.numberAddressEditText)
        profileImage = findViewById(R.id.profileImageView)
        saveButton = findViewById(R.id.saveButton)

        token = Token(this)
        loading = Loading(this)
        loading.openDialog()
        profile()

        profileImage.setOnClickListener {
            try {
                val values = ContentValues()
                values.put(MediaStore.Images.Media.TITLE, "image")
                values.put(MediaStore.Images.Media.DESCRIPTION, "image")
                values.put(MediaStore.Images.Media.SIZE, 5)
                image = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                callCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image)
                callCameraIntent.putExtra(MediaStore.Images.Media.SIZE, 5)
                startActivityForResult(callCameraIntent, 0)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    this,
                    "Terjadi Kesalahan saatmembuka kemera coba ulangi lagi",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        saveButton.setOnClickListener {
            loading.openDialog()
            Timer().schedule(100) {
                try {
                    val body = HashMap<String, String>()
                    body["name"] = name.text.toString()
                    body["province"] = province.text.toString()
                    body["district"] = district.text.toString()
                    body["sub_district"] = subDistrict.text.toString()
                    body["village"] = village.text.toString()
                    body["number_address"] = numberAddress.text.toString()
                    body["description_address"] = descriptionAddress.text.toString()
                    response = UpdateProfileController(body, token.auth).execute().get()
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }

    private fun profile() {
        Timer().schedule(100) {
            response = ProfileController(token.auth).execute().get()
            runOnUiThread {
                when {
                    response["code"] == 200 -> {
                        name.setText(response.getJSONObject("data")["name"].toString())
                        province.setText(response.getJSONObject("data")["province"].toString())
                        district.setText(response.getJSONObject("data")["district"].toString())
                        subDistrict.setText(response.getJSONObject("data")["sub_district"].toString())
                        village.setText(response.getJSONObject("data")["village"].toString())
                        descriptionAddress.setText(response.getJSONObject("data")["description_address"].toString())
                        numberAddress.setText(response.getJSONObject("data")["number_address"].toString())
                        changeProfileImage(response.getJSONObject("data")["image"].toString())
                    }
                    response["code"] == 401 -> {
                        goTo = Intent(applicationContext, MainActivity::class.java)
                        loading.closeDialog()
                        finish()
                        startActivity(goTo)
                    }
                    else -> {
                        loading.closeDialog()
                        finish()
                    }
                }
            }
        }
    }

    private fun changeProfileImage(urlImage: String) {
        if (urlImage.isNotEmpty()) {
            val imageGeneratorController = ImageGeneratorController(urlImage)
            val gitBitmap = imageGeneratorController.execute().get()
            profileImage.setImageBitmap(gitBitmap)
        }
        loading.closeDialog()
    }

    private fun getRealPathFromImageURI(contentUri: Uri?): String {
        val data: Array<String> = Array(100) { MediaStore.Images.Media.DATA }
        val cursor = managedQuery(contentUri, data, null, null, null)
        val columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(columnIndex)
    }

    private fun getImageUri(inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 80, bytes)
        return Uri.parse(
            MediaStore.Images.Media.insertImage(
                this.contentResolver,
                inImage,
                "image",
                null
            )
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            try {
                loading.openDialog()
                Timer().schedule(100) {
                    filePath = getRealPathFromImageURI(image)
                    val convertArray = filePath.split("/").toTypedArray()
                    fileName = convertArray.last()
                    val thumbnails =
                        MediaStore.Images.Media.getBitmap(contentResolver, image)
                    val bitmap = Bitmap.createScaledBitmap(thumbnails, 150, 150, true)
                    runOnUiThread {
                        Handler().postDelayed({
                            profileImage.setImageBitmap(bitmap)
                            loading.closeDialog()
                        }, 100)

                        response =
                            UploadImageController(
                                getRealPathFromImageURI(getImageUri(bitmap)),
                                "image",
                                token.auth
                            ).execute().get()
                        if (response["code"] == 200) {
                            Toast.makeText(
                                applicationContext,
                                "Foto Sukses di upload",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                response["data"].toString(),
                                Toast.LENGTH_LONG
                            ).show()
                            loading.closeDialog()
                        }
                    }
                }
            } catch (ex: Exception) {
                loading.closeDialog()
                Toast.makeText(this, "Ada Kesalah saat mengambil gambar", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "Anda belum mengisi gambar", Toast.LENGTH_LONG).show()
        }
    }
}
