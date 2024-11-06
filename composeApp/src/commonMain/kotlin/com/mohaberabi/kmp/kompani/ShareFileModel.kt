package com.mohaberabi.kmp.kompani


enum class MimeType {
    PDF,
    TEXT,
    IMAGE,
}

class ShareFileModel(
    val mime: MimeType = MimeType.PDF,
    val fileName: String,
    val bytes: ByteArray
)
