package com.mobile.fkpn.controller

import android.os.AsyncTask
import com.mobile.fkpn.model.MapConverter
import com.mobile.fkpn.model.Url
import com.squareup.okhttp.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

class RequestDepositController(
    private var token: String,
    private var body: HashMap<String, String>
) :
    AsyncTask<Void, Void, JSONObject>() {
    override fun doInBackground(vararg params: Void?): JSONObject {
        try {
            val client = OkHttpClient()
            val mediaType: MediaType = MediaType.parse("application/x-www-form-urlencoded")
            val sendBody = RequestBody.create(mediaType, MapConverter().convert(body))
            val request: Request = Request.Builder()
                .url("${Url.get()}deposit/create")
                .method("POST", sendBody)
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("Authorization", "Bearer $token")
                .build()
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
            return JSONObject("{code: 500, data: 'Terjadi kesalahan saat mencoba terhubung ke server'}")
        }
    }
}