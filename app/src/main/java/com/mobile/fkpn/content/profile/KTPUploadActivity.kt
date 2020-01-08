package com.mobile.fkpn.content.profile

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import com.mobile.fkpn.R
import com.mobile.fkpn.controller.UploadImageController
import com.mobile.fkpn.model.Loading
import com.mobile.fkpn.model.Token
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.concurrent.schedule

class KTPUploadActivity : AppCompatActivity() {

    private lateinit var token: Token
    private lateinit var loading: Loading
    private lateinit var response: JSONObject
    private lateinit var imageKTP: ImageView
    private lateinit var imageSelfAndKTP: ImageView
    private var image: Uri? = null
    private var nameBody: String = "ktp_img"
    private var filePath: String = ""
    private var fileName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ktpupload)

        imageKTP = findViewById(R.id.imageKTPView)
        imageSelfAndKTP = findViewById(R.id.imageSelfAndKTPView)
        token = Token(this)
        loading = Loading(this)

        imageKTP.setOnClickListener {
            try {
                nameBody = "ktp_img"
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

        imageSelfAndKTP.setOnClickListener {
            try {
                nameBody = "ktp_img_user"
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
                            if (nameBody == "ktp_img") {
                                imageKTP.setImageBitmap(bitmap)
                            } else {
                                imageSelfAndKTP.setImageBitmap(bitmap)
                            }
                            loading.closeDialog()
                        }, 100)

                        response =
                            UploadImageController(
                                getRealPathFromImageURI(getImageUri(bitmap)),
                                nameBody,
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
