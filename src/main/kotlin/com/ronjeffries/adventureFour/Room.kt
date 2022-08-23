package com.ronjeffries.adventureFour


typealias GoTarget = Pair<String, (GameResponse)->Boolean>

class Room(val name: String) {
    val moves = mutableMapOf<String,GoTarget>().withDefault { Pair(name, {r:GameResponse->true}) }
    var shortDesc = ""
    var longDesc = ""

    // DSL Builders

    fun desc(short: String, long: String) {
        shortDesc = short
        longDesc = long
    }
    fun go(direction: String, roomName: String, allowed: (GameResponse)->Boolean = {r:GameResponse -> true}) {
        moves += direction to Pair(roomName, allowed)
    }

    // Game Play

    fun move(direction: String, response: GameResponse): String {
        val (target, allowed) = moves.getValue(direction)
        return if (allowed(response))
            target
        else
            name
    }

    fun command(cmd: String, response: GameResponse) {
        val name = when(cmd) {
            "s" -> move("s", response)
            "e" -> move("e", response)
            "w" -> move("w", response)
            "n" -> move("n", response)
            "xyzzy" -> move("xyzzy", response)
            else -> "unknown cmd $cmd"
        }
        response.nextRoomName = name
    }

    // Utilities and Other

    val roomReferences: Set<String> get () {
//        return setOf("Y3", "Y2", "clearing", "woods")
        return (moves.values.map {it.first}).toSet()
    }
}
