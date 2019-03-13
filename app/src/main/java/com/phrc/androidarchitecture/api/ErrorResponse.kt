package com.phrc.androidarchitecture.api

class ErrorResponse : Exception {

    val errorCodes: ErrorCodes
    val statusCode: Int

    constructor(errorCodes: ErrorCodes, statusCode: Int){
        this.errorCodes = errorCodes
        this.statusCode = statusCode
    }

    constructor(throwable: Throwable, statusCode: Int) : super(throwable) {
        this.errorCodes = ErrorCodes.THROWABLE
        this.statusCode = statusCode
    }

    constructor(throwable: Throwable, errorCodes : ErrorCodes, statusCode: Int) : super(throwable) {
        this.errorCodes = errorCodes
        this.statusCode = statusCode
    }

    override fun toString(): String {
        return "ErrorResponse{" +
                "errorCodes=" + errorCodes +
                ", statusCode=" + statusCode +
                '}'.toString()
    }
}
