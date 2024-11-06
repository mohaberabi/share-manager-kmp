package com.mohaberabi.kmp.kompani

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

actual class ShareManager(
    private val context: Context,
) {
    actual fun shareText(text: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            flags += Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra(Intent.EXTRA_TEXT, text)

        }
        val choicer = Intent.createChooser(intent, null)
        context.startActivity(choicer)
    }

    actual suspend fun shareFile(
        file: ShareFileModel,
    ): Result<Unit> {
        return kotlin.runCatching {
            withContext(Dispatchers.IO) {
                val savedFile = saveFile(file.fileName, file.bytes)
                val uri = FileProvider.getUriForFile(
                    context,
                    context.packageName + ".provider",
                    savedFile,
                )
                withContext(Dispatchers.Main) {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        putExtra(Intent.EXTRA_STREAM, uri)
                        flags += Intent.FLAG_ACTIVITY_NEW_TASK
                        flags += Intent.FLAG_GRANT_READ_URI_PERMISSION
                        type = file.mime.toAndroidMimeType()
                    }
                    val choicer = Intent.createChooser(intent, null)
                    context.startActivity(choicer)
                }
            }


        }

    }


    private fun saveFile(
        name: String,
        data: ByteArray,
    ): File {
        val cache = context.cacheDir
        val savedFile = File(cache, name)
        savedFile.writeBytes(data)
        return savedFile
    }
}

private fun MimeType.toAndroidMimeType(): String = when (this) {
    MimeType.PDF -> "application/pdf"
    MimeType.TEXT -> "text/plain"
    MimeType.IMAGE -> "image/*"
}

@Composable
actual fun rememberShareManager(): ShareManager {

    val context = LocalContext.current
    return remember {
        ShareManager(context)
    }

}