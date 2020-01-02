package com.mobile.fkpn.controller

import android.os.AsyncTask
import com.mobile.fkpn.model.MapConverter
import com.mobile.fkpn.model.Url
import com.squareup.okhttp.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

class LoginController(private var body: HashMap<String, String>) :
    AsyncTask<Void, Void, JSONObject>() {
    override fun doInBackground(vararg params: Void?): JSONObject {
        try {
            val client = OkHttpClient()
            val mediaType: MediaType = MediaType.parse("application/x-www-form-urlencoded")
            val sendBody = RequestBody.create(mediaType, MapConverter().convert(body))
            val request: Request = Request.Builder()
                .url("${Url.get()}login")
                .method("POST", sendBody)
                .addHeader("X-Requested-With", "XMLHttpRequest")
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
                JSONObject(
                    "{code: ${response.code()}, data: '${convertJSON
                        .getJSONObject("errors")
                        .getJSONArray(
                            convertJSON
                                .getJSONObject("errors")
                                .names()[0]
                                .toString()
                        )[0]
                    }'}"
                )
            }
        } catch (e: Exception) {
            return JSONObject("{code: 500, data: 'Koneksi Anda Hilang'}")
        }
    }
}