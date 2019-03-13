package com.phrc.androidarchitecture.api

import com.phrc.androidarchitecture.R

enum class ErrorCodes constructor(var code: Int, var message: Int) {

    UNKNOWN_ERROR(-1, R.string.UNKNOWN_ERROR),
    THROWABLE(-2, R.string.UNKNOWN_ERROR),
    TIME_OUT(419, R.string.TIME_OUT),
    ERROR_500(500, R.string.ERROR_500);


    companion object {

        fun findByCode(code: Int): ErrorCodes {
            for (errorCodes in values()) {
                if (errorCodes.code == code) {
                    return errorCodes
                }
            }
            return UNKNOWN_ERROR
        }
    }

}
