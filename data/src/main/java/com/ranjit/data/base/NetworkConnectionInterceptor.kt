package com.ranjit.data.base

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import okhttp3.Interceptor
import okhttp3.Response

/*Key points:

NetworkConnectionInterceptor is a class implementing the Interceptor interface from OkHttp.

intercept(chain: Interceptor.Chain): Response is the main function where you intercept and
modify the request before it is sent to the server and the response before it is received by the application.

isInternetAvailable(): Boolean checks for internet connectivity using the ConnectivityManager and NetworkCapabilities.

If there is no internet connectivity, it throws a NoInternetException.

chain.proceed(chain.request()) proceeds with the original request
and returns the response if internet connectivity is available.*/

class NetworkConnectionInterceptor(
    context: Context
) : Interceptor {

    // This class implements OkHttp's Interceptor interface to check for internet connectivity before making a network request.

    private val applicationContext = context.applicationContext

    // The application context is stored for checking connectivity.

    @RequiresApi(Build.VERSION_CODES.M)
    override fun intercept(chain: Interceptor.Chain): Response {

        // The intercept function is called for every request. It allows you to modify the request
        // before it is sent to the server and modify the response before it is received by the application.

        if (!isInternetAvailable())
            throw NoInternetException("Make sure you have an active data connection")

        // Check for internet connectivity using the isInternetAvailable function.
        // If not available, throw a NoInternetException.

        return chain.proceed(chain.request())

        // If internet is available, proceed with the original request and return the response.
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isInternetAvailable(): Boolean {
        var result = false
        val connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

        // Get the ConnectivityManager service from the application context.

        connectivityManager?.let {
            it.getNetworkCapabilities(connectivityManager.activeNetwork)?.apply {

                // Get the network capabilities of the active network.

                result = when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    else -> false
                }

                // Check if the network has either TRANSPORT_WIFI or TRANSPORT_CELLULAR capabilities.

            }
        }
        return result
    }
}
