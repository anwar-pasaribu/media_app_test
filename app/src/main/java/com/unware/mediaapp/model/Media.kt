package com.unware.mediaapp.model

open class Media(
    val mediaId: Long,
    val path: String
) {

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return this.mediaId == (other as Media).mediaId
    }
}