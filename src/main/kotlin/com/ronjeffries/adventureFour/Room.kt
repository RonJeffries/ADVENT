package com.ronjeffries.adventureFour

class Room(val name: String) {
    val moves = mutableMapOf<String,String>().withDefault { name }
    var shortDesc = ""
    var longDesc = ""

    // DSL Builders

    fun desc(short: String, long: String) {
        shortDesc = short
        longDesc = long
    }
    fun go(direction: String, roomName: String) {
        moves += direction to roomName
    }

    // Game Play

    fun move(direction: String) :String {
        return moves.getValue(direction)
    }

    // Utilities and Other

    val roomReferences: Set<String> get () {
        return moves.values.toSet()
    }
}
