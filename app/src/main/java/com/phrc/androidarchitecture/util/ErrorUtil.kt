package com.phrc.androidarchitecture.util

import com.phrc.androidarchitecture.api.ErrorCodes
import com.phrc.androidarchitecture.api.ErrorResponse
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ErrorUtil {

    fun parseError(response: Response<*>): ErrorResponse {
//        val retrofit = AndroidArchitectureApplication.applicationContext.appComponent.retrofit
//
//        val converter = retrofit.responseBodyConverter<ErrorResponse>(ErrorResponse::class.java, arrayOfNulls(0))
//
//        if (response.code() == 500) {
//            return ErrorResponse(ErrorCodes.ERROR_500, 500)
//        }
//
//        try {
//            response.errorBody().use { responseBody ->
//                return if (responseBody == null) {
//                    parseError(NullPointerException("response body can't be null"))
//                } else {
//                    ErrorResponse(converter.convert(responseBody), response.code())
//                }
//            }
//        } catch (e: IOException) {
//            return parseError(e)
//        }
        return ErrorResponse(ErrorCodes.ERROR_500, 500)
    }

    fun parseError(throwable: Throwable): ErrorResponse {
        return if (throwable is UnknownHostException || throwable is SocketTimeoutException) {
            ErrorResponse(throwable, ErrorCodes.TIME_OUT, 500)

        } else {
            ErrorResponse(throwable, 500)
        }
    }

    fun parseError(errorCode: Int, status: Int): ErrorResponse {
        return ErrorResponse(ErrorCodes.findByCode(errorCode), status)
    }

    fun parseError(errorCodes: ErrorCodes, statusCode: Int): ErrorResponse {
        return ErrorResponse(errorCodes, statusCode)
    }
}