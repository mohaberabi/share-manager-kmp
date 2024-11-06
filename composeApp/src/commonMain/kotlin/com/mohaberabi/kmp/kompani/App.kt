package com.mohaberabi.kmp.kompani

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import kompani.composeapp.generated.resources.Res
import kompani.composeapp.generated.resources.compose_multiplatform
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
@Preview
fun App() {
    val shareManager = rememberShareManager()
    val scope = rememberCoroutineScope()
    var textData by remember { mutableStateOf("") }
    MaterialTheme {
        Column(
            Modifier.fillMaxSize().padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Type the content you wish to share  ")
                },
                value = textData,
                onValueChange = { textData = it },
            )
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    scope.launch {
                        val shared = ShareFileModel(
                            fileName = "testfile.txt",
                            bytes = textData.encodeToByteArray()
                        )
                        shareManager.shareFile(shared)
                    }
                },
            ) {
                Text("Share as file")
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    scope.launch {

                        shareManager.shareText(textData)
                    }
                },
            ) {
                Text("Share as text")
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    scope.launch {
                        val bytes = Res.readBytes("files/compose.png")
                        val shared = ShareFileModel(
                            fileName = "compose.png",
                            bytes = bytes,
                        )
                        shareManager.shareFile(shared)
                    }
                },
            ) {
                Text("Share simple  image")
            }
        }
    }
}