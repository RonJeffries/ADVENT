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

    fun command(cmd: String, response: GameResponse): Unit {
        val name = when(cmd) {
            "s" -> move("s")
            "n" -> move("n")
            "xyzzy" -> move("xyzzy")
            else -> "unknown cmd $cmd"
        }
        response.nextRoomName = name
    }

    // Utilities and Other

    val roomReferences: Set<String> get () {
        return moves.values.toSet()
    }
}
