package com.tugas.tugaspam3

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform