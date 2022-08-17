package com.ronjeffries.adventureFour

class Room(val name: String) {
    val moves = mutableListOf<Pair<String,String>>()
    var shortDesc = ""
    var longDesc = ""

    val roomReferences: Set<String> get () {
        return moves.map { it.second}.toSet()
    }
    fun go(direction: String, roomName: String) {
        moves += direction to roomName
    }

    fun move(direction: String) :String {
        return moves.first { it.first == direction}.second
    }

    fun desc(short: String, long: String) {
        shortDesc = short
        longDesc = long
    }
}
