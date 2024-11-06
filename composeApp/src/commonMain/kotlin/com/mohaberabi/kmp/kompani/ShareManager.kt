package com.mohaberabi.kmp.kompani

import androidx.compose.runtime.Composable

expect class ShareManager {


    fun shareText(text: String)
    suspend fun shareFile(file: ShareFileModel): Result<Unit>
}


@Composable
expect fun rememberShareManager(): ShareManager