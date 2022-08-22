package com.ronjeffries.adventureFour

class Room(val name: String) {
    val moves = mutableListOf<Pair<String,String>>()
    var shortDesc = ""
    var longDesc = ""

    // DSL Builders

    fun desc(short: String, long: String) {
        shortDesc = short
        longDesc = long
    }
    fun move(direction: String) :String {
        return moves.first { it.first == direction}.second
    }

    // Game Play

    fun go(direction: String, roomName: String) {
        moves += direction to roomName
    }

    // Utilities and Other

    val roomReferences: Set<String> get () {
        return moves.map { it.second}.toSet()
    }
}
