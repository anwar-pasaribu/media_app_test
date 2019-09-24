package com.unware.mediaapp.model

data class RequestStatus(
    val state: Int,
    val throwable: Throwable
) {
    companion object {
        const val LOADING = 0
        const val FINISHED = 1
        const val ERROR = 2
    }
}
