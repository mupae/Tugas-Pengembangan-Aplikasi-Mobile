package com.example.newfeedsimulator

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform