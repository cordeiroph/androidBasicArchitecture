package com.phrc.androidarchitecture.ui.base

import android.content.Context
import android.util.Log
import android.view.View

import com.google.android.material.snackbar.Snackbar
import com.phrc.androidarchitecture.api.ErrorCodes
import com.phrc.androidarchitecture.api.RequestStatus
import com.phrc.androidarchitecture.repository.DataRequestModel

class DataRequestUIHandler<T : DataRequestModel<O>, O> {

    var context: Context? = null
    var msgView: View? = null
    internal var successCallback: SuccessCallback<O>
    internal var errorCallback: ErrorCallback<T>? = null
    internal var progressCallback: ProgressCallback? = null
    internal var initialStateCallback: InitialStateCallback? = null

    interface SuccessCallback<O> {
        fun onSuccess(obj: O)
    }

    interface ErrorCallback<T> {
        fun onError(requestUiModel: T)
    }

    interface ProgressCallback {
        fun onStartProgress()
        fun onEndProgress()
    }

    interface InitialStateCallback {
        fun onInitialState()
    }

    constructor(context: Context, msgView: View, successCallback: SuccessCallback<O>) {
        this.context = context
        this.msgView = msgView
        this.successCallback = successCallback
    }

    constructor(context: Context, msgView: View, successCallback: SuccessCallback<O>, progressCallback: ProgressCallback) {
        this.context = context
        this.msgView = msgView
        this.successCallback = successCallback
        this.progressCallback = progressCallback
    }

    constructor(context: Context, msgView: View, successCallback: SuccessCallback<O>, errorCallback: ErrorCallback<T>) {
        this.context = context
        this.msgView = msgView
        this.successCallback = successCallback
        this.errorCallback = errorCallback
    }

    constructor(context: Context, msgView: View, successCallback: SuccessCallback<O>, errorCallback: ErrorCallback<T>, progressCallback: ProgressCallback) {
        this.context = context
        this.msgView = msgView
        this.successCallback = successCallback
        this.errorCallback = errorCallback
        this.progressCallback = progressCallback
    }

    fun handleResult(requestUiModel: T) {
        Log.e("HandleResult", requestUiModel.requestStatus.toString())
        when (requestUiModel.requestStatus) {
            RequestStatus.ERROR -> onError(requestUiModel)
            RequestStatus.SUCCESS -> onSuccess(requestUiModel.content!!)
            RequestStatus.IN_PROGRESS -> onProgress()
            RequestStatus.INITIAL_STATUS -> onInitialState()
        }
    }

    protected fun onSuccess(obj: O) {
        if (progressCallback != null) {
            progressCallback!!.onEndProgress()
        }
        successCallback.onSuccess(obj)
    }

    protected fun onError(requestUiModel: T) {
        Log.e("ApiRequestHandler", "Error")
        if (progressCallback != null) {
            progressCallback!!.onEndProgress()
        }
        if (errorCallback != null) {
            errorCallback!!.onError(requestUiModel)
        }
        if (msgView != null && context != null) {
            if (requestUiModel.errorResponse!!.errorCodes === ErrorCodes.THROWABLE) {
                requestUiModel.errorResponse!!.printStackTrace()
            }
            notification(requestUiModel.errorResponse!!.errorCodes.message)
        }
    }

    protected fun onProgress() {
        Log.e("ApiRequestHandler", "Progress")
        if (progressCallback != null) {
            progressCallback!!.onStartProgress()
        }
    }

    protected fun onInitialState() {
        Log.e("ApiRequestHandler", "Initial state")
        if (initialStateCallback != null) {
            initialStateCallback!!.onInitialState()
        }
    }

    protected fun notification(msg: String) {
        Snackbar.make(msgView!!, msg, Snackbar.LENGTH_LONG).show()
    }

    protected fun notification(msg: Int, vararg objects: Any) {
        if (msg == 0) {
            throw IllegalArgumentException("msg id cant be 0")
        }
        notification(context!!.getString(msg, *objects))
    }
}
