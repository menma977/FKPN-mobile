package com.mobile.fkpn.content.menu

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.mobile.fkpn.R
import com.mobile.fkpn.controller.ImageGeneratorController

class ProfileActivity : AppCompatActivity() {

    private lateinit var imageGeneratorController: ImageGeneratorController
    private lateinit var profileImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profileImage = findViewById(R.id.profileImage)

        changeProfileImage()
    }

    private fun changeProfileImage() {
        val url = "https://upload.wikimedia.org/wikipedia/commons/f/f9/Google_Lens_-_new_logo.png"
        imageGeneratorController = ImageGeneratorController(url)
        val gitBitmap = imageGeneratorController.execute().get()
        profileImage.setImageBitmap(gitBitmap)
    }
}
