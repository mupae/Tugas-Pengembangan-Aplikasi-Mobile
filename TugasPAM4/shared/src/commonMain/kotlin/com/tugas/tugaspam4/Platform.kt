package com.tugas.tugaspam4

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform