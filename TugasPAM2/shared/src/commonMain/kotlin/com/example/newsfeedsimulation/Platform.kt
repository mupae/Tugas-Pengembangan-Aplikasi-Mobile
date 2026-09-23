package com.example.newsfeedsimulation

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform