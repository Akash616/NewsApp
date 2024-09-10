package com.akashgupta.newsapp.util

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {

    /*Class that recommended by GOOGLE to wrap around our network responses.
    And that will be generic(<>) class and it is very useful to differentiate b/w successful and
    error responses and also helps us to handle the loading state(so when we make a request
    that we can show progress bar while the response is processing and when we get the response
    hide progress bar and tell us whether the response was successful or an error).*/

    /* sealed class -> it is a class, kind of an abstract class but we can define which class
    * are allowed to inherit from that Resource class*/

    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()

}