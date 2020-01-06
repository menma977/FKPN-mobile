package com.mobile.fkpn.controller

import android.os.AsyncTask
import com.mobile.fkpn.model.Url
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

class StorePackageController(private var token: String, private var body: String) :
    AsyncTask<Void, Void, JSONObject>() {
    override fun doInBackground(vararg params: Void?): JSONObject {
        try {
            val client = OkHttpClient()
            val request: Request = Request.Builder()
                .url("${Url.get()}invest/create/" + body)
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
                JSONObject("{code: ${response.code()}, data: '${convertJSON["response"]}'}")
            } else {
                JSONObject().put("code", response.code()).put(
                    "data", convertJSON
                        .getJSONObject("errors")
                        .getJSONArray(
                            convertJSON
                                .getJSONObject("errors")
                                .names()[0]
                                .toString()
                        )[0]
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return JSONObject("{code: 500, data: 'Koneksi Anda Hilang'}")
        }
    }
}