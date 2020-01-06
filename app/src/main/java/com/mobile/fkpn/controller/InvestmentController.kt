package com.mobile.fkpn.controller

import android.os.AsyncTask
import com.mobile.fkpn.model.Url
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

class InvestmentController(private var token: String) : AsyncTask<Void, Void, JSONArray>() {
    override fun doInBackground(vararg params: Void?): JSONArray {
        val json = JSONArray()
        try {
            val client = OkHttpClient()
            val request: Request = Request.Builder()
                .url("${Url.get()}invest")
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
                json.put(200)
                json.put(convertJSON["lastInvest"])
                json.put(convertJSON["investList"])
                json
            } else {
                json.put(422)
                json.put("Koneksi Anda Hilang")
                json
            }
        } catch (e: Exception) {
            e.printStackTrace()
            json.put(500)
            json.put("Koneksi Anda Hilang")
            return json
        }
    }
}