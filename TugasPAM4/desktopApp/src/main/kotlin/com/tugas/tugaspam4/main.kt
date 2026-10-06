package com.tugas.tugaspam4

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "TugasPAM4",
    ) {
        App()
    }
}