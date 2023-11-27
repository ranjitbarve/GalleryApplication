package com.ranjit.data.base

import com.ranjit.data.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
/*
Key points:

AuthenticationInterceptor is a class implementing the Interceptor interface from OkHttp.

intercept(chain: Interceptor.Chain): Response is the main function where you intercept and
modify the request before it is sent to the server and the response before it is received by the application.

val builder = chain.request().newBuilder() creates a new request builder based on the original request.

builder.addHeader(HEADER_NAME, "Client-ID ${BuildConfig.CLIENT_ID}") adds an Authorization header to the request,
using the Client-ID from BuildConfig.

val request = builder.build() builds the modified request.

Timber.d("AuthenticationInterceptor:${request}") logs information about the modified request using Timber.

return chain.proceed(request) proceeds with the modified request and returns the response.*/
class AuthenticationInterceptor : Interceptor {

    // This class implements OkHttp's Interceptor interface for handling authentication in network requests.

    override fun intercept(chain: Interceptor.Chain): Response {

        // The intercept function is called for every request. It allows you to modify the request
        // before it is sent to the server and modify the response before it is received by the application.

        val builder = chain.request().newBuilder()

        // Create a new request builder based on the original request.

        builder.addHeader(HEADER_NAME, "Client-ID ${BuildConfig.CLIENT_ID}")

        // Add an Authorization header to the request with the Client-ID from BuildConfig.

        val request = builder.build()

        // Build the modified request.

        Timber.d("AuthenticationInterceptor:${request}")

        // Log information about the modified request using Timber.

        return chain.proceed(request)

        // Proceed with the modified request and return the response.
    }

    private companion object {
        private const val HEADER_NAME = "Authorization"
    }
}
