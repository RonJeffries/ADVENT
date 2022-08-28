package com.ronjeffries.adventureFour


typealias GoTarget = Pair<String, (World)->Boolean>

class Room(val roomName: String) {
    val contents = mutableSetOf<String>()
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

    fun itemString(): String {
        return contents.joinToString(separator = "") {"You find $it.\n"}
    }

    fun move(verb: String, world: World): String {
        val (targetName, allowed) = moves.getValue(verb)
        return if (allowed(world))
            targetName
        else
            roomName
    }

    fun command(cmd: String, world: World) {
        val name = when(cmd) {
            "take axe" -> take(cmd, "axe", world)
            "take bottle" -> take(cmd, "bottle", world)
            "take cows" -> take(cmd, "cows", world)
            "inventory" -> inventory(world)
            "s","e","w","n" -> move(cmd, world)
            "xyzzy" -> move("xyzzy", world)
            "cast wd40" -> castSpell("wd40", world)
            else -> "unknown cmd $cmd"
        }
        world.response.nextRoomName = name
    }

    private fun inventory(world: World): String {
        world.showInventory()
        return roomName
    }

    private fun take(verb: String, noun: String, world: World): String {
        world.take(noun)
        world.response.say("$noun taken.")
        return roomName
    }

    private fun castSpell(spell: String, world: World): String {
        world.flags.get("unlocked").set(true)
        world.response.say("The magic wd40 works! The padlock is unlocked!")
        return roomName
    }

    fun item(thing: String) {
        contents+=thing
    }

    // Utilities and Other

    val roomReferences: Set<String> get () {
        return moves.values.map {it.first}.toSet()
    }
}
