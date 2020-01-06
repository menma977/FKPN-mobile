package com.mobile.fkpn.controller

import android.os.AsyncTask
import com.mobile.fkpn.model.Url
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

class BonusController {
    class Get(private var token: String) : AsyncTask<Void, Void, JSONObject>() {
        override fun doInBackground(vararg params: Void?): JSONObject {
            try {
                val client = OkHttpClient()
                val request: Request = Request.Builder()
                    .url("${Url.get()}bonus")
                    .method("GET", null)
                    .addHeader("X-Requested-With", "XMLHttpRequest")
                    .addHeader(
                        "Authorization",
                        "Bearer $token"
                    ).build()
                val response: Response = client.newCall(request).execute()
                val input =
                    BufferedReader(InputStreamReader(response.body().byteStream()))

                val inputData: String = input.readLine()
                val convertJSON = JSONObject(inputData)
                input.close()
                return if (response.isSuccessful) {
                    JSONObject().put("code", response.code()).put("data", convertJSON["response"])
                } else {
                    JSONObject().put("code", response.code()).put("data", convertJSON["response"])
                }
            } catch (e: Exception) {
                return JSONObject().put("code", 500).put("data", "Koneksi Anda Hilang")
            }
        }
    }
}