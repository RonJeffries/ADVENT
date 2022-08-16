package com.ronjeffries.adventureFour

class Room(val name: String) {
    val moves = mutableListOf<Pair<String,String>>()
    fun go(direction: String, roomName: String) {
        moves += Pair(direction, roomName)
    }
}
