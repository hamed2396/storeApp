package com.example.storeapp.utils.network

import com.crazylegend.kotlinextensions.ifNotNull
import com.example.storeapp.models.ErrorResponse
import com.example.storeapp.utils.network.NetworkStatus.Data
import com.example.storeapp.utils.network.NetworkStatus.Error
import com.google.gson.Gson
import retrofit2.Response
import javax.inject.Inject

class NetworkResponse @Inject constructor() {

    fun <T> handleResponse(response: Response<T>): NetworkStatus<T> {
        return when {
            response.isSuccessful -> Data(response.body()!!)
            response.code() == 401 -> Error("You Are Not Authorized")
            response.code() == 422 -> {
                var errorMessage = ""

                response.errorBody()?.let {

                    val errorResponse = Gson().fromJson(
                        response.errorBody()!!.charStream(),
                        ErrorResponse::class.java
                    )
                  //  val message = errorResponse.message
                    val errors = errorResponse.errors
                    errors?.forEach { (_, errorsList) ->
                        errorMessage = errorsList.joinToString()
                    }

                }
                Error(errorMessage)
            }

            response.code() == 500 -> Error("Try Again")

            else -> {
                Error(response.message())
            }
        }
    }
}