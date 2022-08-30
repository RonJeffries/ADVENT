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

    fun move(verb: String, noun: String, world: World): String {
        val (targetName, allowed) = moves.getValue(verb)
        return if (allowed(world))
            targetName
        else
            roomName
    }

    fun command(cmd: String, world: World) {
        val c = Command(cmd).validate()
        val action: (String, String, World)->String = when(c.verb) {
            else -> when(cmd) {
                "inventory" -> ::inventory
                "take axe" -> ::take
                "take bottle" -> ::take
                "take cows" -> ::take
                "s", "e", "w", "n" -> ::move
                "xyzzy" -> ::move
                "cast wd40" -> ::castSpell
                else -> ::unknown
            }
        }
        val name = action(cmd, cmd, world)
        world.response.nextRoomName = name
    }

    private fun unknown(verb:String, noun:String, world: World): String {
        world.response.say("unoknown command $verb")
        return "unknown cmd $verb"
    }

    private fun inventory(verb: String, noun: String, world: World): String {
        world.showInventory()
        return roomName
    }

    private fun take(verb: String, noun: String, world: World): String {
        // interim hackery waiting for new parser
        val words = noun.split(" ")
        val realNoun = words.last()
        val done = contents.remove(realNoun)
        if ( done ) {
            world.addToInventory(realNoun)
            world.response.say("$realNoun taken.")
        } else {
            world.response.say("I see no $realNoun here!")
        }
        return roomName
    }

    private fun castSpell(noun: String, verb: String, world: World): String {
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
