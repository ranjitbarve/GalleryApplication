package com.ranjit.data.base

import java.io.IOException

class ApiException(message: String) : IOException(message)

// This class represents an exception that should be thrown when there is an issue related to an API call.
// It extends IOException, indicating that it is an IO-related exception.
// The constructor takes a message as a parameter, allowing you to provide details about the exception.

class NoInternetException(message: String) : IOException(message)

// This class represents an exception that should be thrown when there is no internet connectivity.
// It extends IOException, indicating that it is an IO-related exception.
// The constructor takes a message as a parameter, allowing you to provide details about the exception.
