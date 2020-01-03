package com.mobile.fkpn.controller

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import java.lang.Exception
import java.net.URL

class ImageGeneratorController(private var url: String) : AsyncTask<Void, Void, Bitmap>() {
    override fun doInBackground(vararg params: Void?): Bitmap {
        return try {
            val url = URL(url)
            BitmapFactory.decodeStream(url.openConnection().getInputStream())
        } catch (e: Exception) {
            e.printStackTrace()
            val url =
                URL("https://upload.wikimedia.org/wikipedia/commons/f/f9/Google_Lens_-_new_logo.png")
            BitmapFactory.decodeStream(url.openConnection().getInputStream())
        }
    }
}