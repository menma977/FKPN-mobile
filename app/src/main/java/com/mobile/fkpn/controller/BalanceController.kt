package com.mobile.fkpn.controller

import android.os.AsyncTask
import com.mobile.fkpn.model.Url
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

class BalanceController(private var token: String) : AsyncTask<Void, Void, JSONObject>() {
    override fun doInBackground(vararg params: Void?): JSONObject {
        try {
            val client = OkHttpClient()
            val request: Request = Request.Builder()
                .url("${Url.get()}user/balance")
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
                convertJSON.put("code", response.code())
                convertJSON
            } else {
                JSONObject("{code: ${response.code()}, data: 'Sesi anda Sudah Habis'}")
            }
        } catch (e: Exception) {
            return JSONObject("{code: 500, data: 'Koneksi Anda Hilang'}")
        }
    }
}