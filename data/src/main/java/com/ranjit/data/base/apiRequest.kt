package com.ranjit.data.base

import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

/*Key points:

SafeApiRequest is an abstract class providing a common mechanism for making safe API requests.

apiRequest is a generic function that takes a suspend function representing the API call and
returns the parsed response.

The function logs information about the response, including the message, body, error body, and raw response.

If the response is successful (HTTP status code 2xx), it returns the body of the response.

If the response is not successful, it attempts to extract an error message from the error body.
If successful, it throws an ApiException with the extracted error message;
otherwise, it throws an ApiException with a default message.*/
abstract class SafeApiRequest {

    // This abstract class provides a common mechanism for making safe API requests.

    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): T {

        // The apiRequest function is a generic function that takes a suspend function,
        // representing the API call, and returns the parsed response.

        val response = call.invoke()

        // Invoke the API call to get the response.

        Log.d("message : ",""+response.message())
        Log.d("body : ",""+response.body())
        Log.d("error : ",""+response.errorBody())
        Log.d("message : ",""+response.raw())

        // Log information about the response.

        if (response.isSuccessful) {
            // If the response is successful (HTTP status code 2xx),
            // return the body of the response.

            return response.body()!!
        } else {
            // If the response is not successful, handle the error.

            val error = response.errorBody()?.string()

            val message = StringBuilder()
            error?.let {
                try {
                    // Try to extract an error message from the error body.
                    message.append(JSONObject(it).getString("status_message"))
                } catch (e: JSONException) {
                    // If there's an issue extracting the error message, do nothing.
                }
            }

            throw ApiException(message.toString())
            // Throw an ApiException with the extracted or default error message.
        }
    }
}

