package com.ronjeffries.adventureFour

class Room(val name: String) {
    val roomReferences: Set<String> get () {
        return moves.map { it.second}.toSet()
    }
    val moves = mutableListOf<Pair<String,String>>()
    fun go(direction: String, roomName: String) {
        moves += Pair(direction, roomName)
    }

    fun move(direction: String) :String {
        return moves.first { it.first == direction}.second
    }
}
