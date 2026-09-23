package com.example.newfeedsimulator

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

data class Berita(val id: Int, val kategori: String)

fun main() = runBlocking {
    println("NFS")

    val jumlahBerita = MutableStateFlow(0)

    launch {
        jumlahBerita.collect { jumlah ->
            println("Total berita Kriminal yang dibaca: $jumlah")
        }
    }

    val newFlow = flow {
        var id = 1
        // Perbaikan: Memisahkan setiap elemen kategori menjadi string yang berbeda
        val kategori = listOf("Kriminal", "Teknologi", "Politik", "Game")

        while (true) {
            delay(2000)
            val beritaBaru = Berita(id, kategori.random())
            // Perbaikan: Simbol template string adalah '$', bukan '&'. Typo 'kategoti' diperbaiki.
            println("Berita baru : ${beritaBaru.kategori}")
            emit(beritaBaru)
            id++
        }
    }

    suspend fun fetchBeritaAsynch(id: Int): String {
        delay(1500)
        return "Detail berita untuk ID-$id."
    }

    newFlow
        .filter { berita ->
            berita.kategori == "Kriminal"
        }
        .map { berita ->
            val format = "[${berita.kategori.uppercase()}]"
            Pair(berita.id, format)
        }
        .collect { (id, tampilan) ->
            println("Tampil di berita: $tampilan")

            launch {
                val detail = fetchBeritaAsynch(id)
                println("Berita mengenai tindak kriminal")

                jumlahBerita.value += 1
            }
        }
}