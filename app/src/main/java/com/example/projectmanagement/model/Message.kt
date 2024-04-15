package com.example.projectmanagement.model

data class Message(
    val senderUid: String? = null,
    val receiverUid: String? = null,
    val message: String? = null,
    val timestamp: Long? = null
) {
    constructor() : this("", "", "", 0L)
}
