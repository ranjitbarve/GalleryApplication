package com.ranjit.data.base

import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

abstract class SafeApiRequest {

    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): T {

        val response = call.invoke()
        Log.d("message : ",""+response.message())
        Log.d("body : ",""+response.body())
        Log.d("error : ",""+response.errorBody())
        Log.d("message : ",""+response.raw())
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            val error = response.errorBody()?.string()

            val message = StringBuilder()
            error?.let {
                try {
                    message.append(JSONObject(it).getString("status_message"))
                } catch (e: JSONException) {
                }
            }
            throw ApiException(message.toString())
        }
    }

}
