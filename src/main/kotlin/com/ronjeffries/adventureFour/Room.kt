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

    fun go(verb: String, noun: String, world: World): String {
        val (targetName, allowed) = moves.getValue(noun)
        return if (allowed(world))
            targetName
        else
            roomName
    }

    fun command(cmd: String, world: World) {
        val c = Command(cmd).validate()
        val action: (String, String, World)->String = when(c.verb) {
            "inventory" -> ::inventory
            "take" -> ::take
            "go" -> this::go
            "say" -> ::castSpell
            else -> ::unknown
        }
        val name:String = action(c.verb, c.noun, world)
        world.response.nextRoomName = name
    }

    private fun unknown(verb:String, noun:String, world: World): String {
        world.response.say("unknown command $verb")
        return "unknown cmd $verb"
    }

    private fun inventory(verb: String, noun: String, world: World): String {
        world.showInventory()
        return roomName
    }

    private fun take(verb: String, noun: String, world: World): String {
        val done = contents.remove(noun)
        if ( done ) {
            world.addToInventory(noun)
            world.response.say("$noun taken.")
        } else {
            world.response.say("I see no $noun here!")
        }
        return roomName
    }

    private fun castSpell(verb: String, noun: String, world: World): String {
        val returnRoom = when (noun) {
            "wd40" -> {
                world.flags.get("unlocked").set(true)
                world.response.say("The magic wd40 works! The padlock is unlocked!")
                roomName
            }
            "xyzzy" -> {
                val (targetName, allowed) = moves.getValue(noun)
                return if (allowed(world))
                    targetName
                else
                    roomName
            }
            else -> {
                world.response.say("Nothing happens here.")
                roomName
            }
        }
        return returnRoom
    }

    fun item(thing: String) {
        contents+=thing
    }

    // Utilities and Other

    val roomReferences: Set<String> get () {
        return moves.values.map {it.first}.toSet()
    }
}
