package com.ronjeffries.adventureFour


typealias GoTarget = Pair<String, (World)->Boolean>

class Room(val roomName: String) {
    val moves = mutableMapOf<String,GoTarget>().withDefault { Pair(roomName, { r:World->true}) }
    var shortDesc = ""
    var longDesc = ""

    // DSL Builders

    fun desc(short: String, long: String) {
        shortDesc = short
        longDesc = long
    }
    fun go(direction: String, roomName: String, allowed: (World)->Boolean = {r:World -> true}) {
        moves += direction to Pair(roomName, allowed)
    }

    // Game Play

    fun move(direction: String, world: World): String {
        val (targetName, allowed) = moves.getValue(direction)
        return if (allowed(world))
            targetName
        else
            roomName
    }

    fun command(cmd: String, world: World) {
        val name = when(cmd) {
            "s","e","w","n" -> move(cmd, world)
            "xyzzy" -> move("xyzzy", world)
            "cast wd40" -> castSpell("wd40", world)
            else -> "unknown cmd $cmd"
        }
        world.response.nextRoomName = name
    }

    private fun castSpell(spell: String, world: World): String {
        world.status.put("unlocked", true)
        world.response.say("The magic wd40 works! The padlock is unlocked!")
        return roomName
    }

    // Utilities and Other

    val roomReferences: Set<String> get () {
        return moves.values.map {it.first}.toSet()
    }
}
