package com.ronjeffries.adventureFour


typealias GoTarget = Pair<String, ()->Boolean>

class Room(val name: String) {
    val moves = mutableMapOf<String,GoTarget>().withDefault { Pair(name, {true}) }
    var shortDesc = ""
    var longDesc = ""

    // DSL Builders

    fun desc(short: String, long: String) {
        shortDesc = short
        longDesc = long
    }
    fun go(direction: String, roomName: String, allowed: ()->Boolean = {true}) {
        moves += direction to Pair(roomName, allowed)
    }

    // Game Play

    fun move(direction: String) :String {
        val (target,allowed) = moves.getValue(direction)
        return if (allowed())
            target
        else
            name
    }

    fun command(cmd: String, response: GameResponse): Unit {
        val name = when(cmd) {
            "s" -> move("s")
            "n" -> move("n")
            "e" -> move("e")
            "w" -> move("w")
            "xyzzy" -> move("xyzzy")
            else -> "unknown cmd $cmd"
        }
        response.nextRoomName = name
    }

    // Utilities and Other

    val roomReferences: Set<String> get () {
        return setOf("Y3", "Y2", "clearing", "woods")
        //return moves.values.toSet()
    }
}
