package com.mohaberabi.kmp.kompani

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import platform.Foundation.NSData
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.dataWithBytes
import platform.Foundation.writeToFile
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication

actual class ShareManager {


    actual fun shareText(text: String) {
        val activityViewController = UIActivityViewController(
            listOf(text),
            null
        )
        UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
            activityViewController,
            animated = true,
            completion = null
        )
    }

    actual suspend fun shareFile(
        file: ShareFileModel,
    ): Result<Unit> {
        return kotlin.runCatching {
            val url = withContext(Dispatchers.IO) {
                saveFile(file.bytes, file.fileName)
            }
            val activityViewController = UIActivityViewController(
                listOf(url),
                null
            )
            UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
                activityViewController,
                animated = true,
                completion = null
            )
        }

    }


    @OptIn(ExperimentalForeignApi::class)
    private fun saveFile(
        bytes: ByteArray,
        name: String,
    ): NSURL? {
        val tempDir = NSTemporaryDirectory()
        val sharedFile = tempDir + name
        val saved = bytes.usePinned {
            val nsData = NSData.dataWithBytes(it.addressOf(0), bytes.size.toULong())
            nsData.writeToFile(sharedFile, true)
        }
        return if (saved) {
            NSURL.fileURLWithPath(sharedFile)
        } else {
            null
        }

    }
}


@Composable
actual fun rememberShareManager(): ShareManager {
    return remember { ShareManager() }
}