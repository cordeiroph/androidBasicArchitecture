package com.phrc.androidarchitecture.repository


import com.phrc.androidarchitecture.api.ErrorResponse
import com.phrc.androidarchitecture.api.RequestStatus
import com.phrc.androidarchitecture.util.ErrorUtil
import retrofit2.Response

class DataRequestModel<T> private constructor(val requestStatus: RequestStatus?, val errorResponse: ErrorResponse?, val content: T?) {

    fun <T> convert(content: T): DataRequestModel<T> {
        return DataRequestModel(this.requestStatus, this.errorResponse, content)
    }

    fun <T> convert(tClass: Class<T>): DataRequestModel<T> {
        return DataRequestModel(this.requestStatus, this.errorResponse, null)
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is DataRequestModel<*>) return false

        val that = o as DataRequestModel<*>?

        if (requestStatus !== that!!.requestStatus) return false
        if (if (errorResponse != null) errorResponse != that!!.errorResponse else that!!.errorResponse != null)
            return false
        return if (content != null) content == that.content else that.content == null
    }

    override fun hashCode(): Int {
        var result = requestStatus?.hashCode() ?: 0
        result = 31 * result + (errorResponse?.hashCode() ?: 0)
        result = 31 * result + (content?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "DataRequestModel{" +
                "requestStatus=" + requestStatus +
                ", errorResponse=" + errorResponse +
                ", content=" + content +
                '}'.toString()
    }

    companion object {

        fun <T> isSuccessful(dataRequestModel: DataRequestModel<T>): Boolean {
            return dataRequestModel.requestStatus == RequestStatus.SUCCESS
        }

        fun <T> isInProgress(dataRequestModel: DataRequestModel<T>): Boolean {
            return dataRequestModel.requestStatus == RequestStatus.IN_PROGRESS
        }

        fun <T> inProgress(): DataRequestModel<T> {
            return DataRequestModel(RequestStatus.IN_PROGRESS, null, null)
        }

        fun <T> initialStates(): DataRequestModel<T> {
            return DataRequestModel(RequestStatus.INITIAL_STATUS, null, null)
        }

        fun <R : Response<T>, T> onSuccess(response: R): DataRequestModel<T> {
            return DataRequestModel(RequestStatus.SUCCESS, null, response.body())
        }

        fun <T> onSuccess(content: T): DataRequestModel<T> {
            return DataRequestModel(RequestStatus.SUCCESS, null, content)
        }

        fun <T> onError(throwable: Throwable): DataRequestModel<T> {
            return DataRequestModel(RequestStatus.ERROR, ErrorUtil.parseError(throwable), null)
        }

        fun <T> onError(errorResponse: ErrorResponse): DataRequestModel<T> {
            return DataRequestModel(RequestStatus.ERROR, errorResponse, null)
        }

        fun <R : Response<T>, T> onError(response: R): DataRequestModel<T> {
            return DataRequestModel(RequestStatus.ERROR, ErrorUtil.parseError(response), null)
        }
    }
}
