package com.unware.mediaapp.model

data class RequestStatus(
    val state: Int,
    val message: String,
    val throwable: Throwable?
) {
    constructor(state: Int, msg: String) : this(state, msg, null)

    companion object {
        const val LOADING = 0
        const val FINISHED = 1
        const val ERROR = 2
        const val NODATA = 3

        fun loading(msg: String) = RequestStatus(LOADING, msg)
        fun finished(msg: String) = RequestStatus(FINISHED, msg)
        fun nodata(msg: String) = RequestStatus(NODATA, msg)
        fun error(msg: String) = RequestStatus(ERROR, msg)
    }
}
