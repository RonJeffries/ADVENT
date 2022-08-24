package com.ronjeffries.adventureFour


typealias GoTarget = Pair<String, (GameResponse)->Boolean>

class Room(val roomName: String) {
    val moves = mutableMapOf<String,GoTarget>().withDefault { Pair(roomName, { r:GameResponse->true}) }
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
        val (targetName, allowed) = moves.getValue(direction)
        return if (allowed(response))
            targetName
        else
            roomName
    }

    fun command(cmd: String, response: GameResponse) {
        val name = when(cmd) {
            "s","e","w","n" -> move(cmd, response)
            "xyzzy" -> move("xyzzy", response)
            else -> "unknown cmd $cmd"
        }
        response.nextRoomName = name
    }

    // Utilities and Other

    val roomReferences: Set<String> get () {
        return moves.values.map {it.first}.toSet()
    }
}
