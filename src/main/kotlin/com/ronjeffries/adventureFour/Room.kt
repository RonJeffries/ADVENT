package com.ronjeffries.adventureFour


typealias GoTarget = Pair<String, (World)->Boolean>

class Room(val roomName: String) {
    val contents = mutableSetOf<String>()
    private val moves = mutableMapOf<String,GoTarget>().withDefault { Pair(roomName) { _: World -> true } }
    var shortDesc = ""
    var longDesc = ""

    // DSL Builders

    fun desc(short: String, long: String) {
        shortDesc = short
        longDesc = long
    }
    fun go(direction: String, roomName: String, allowed: (World)->Boolean = { _:World -> true}) {
        moves += direction to Pair(roomName, allowed)
    }

    // Game Play

    fun command(cmd: String, world: World) {
        val command = Command(cmd).validate()
        val action: (Command, World)->String = when(command.verb) {
            "inventory" -> inventory
            "take" -> ::take
            "go" -> this::move
            "say" -> ::castSpell
            else -> ::unknown
        }
        val name:String = action(command, world)
        world.response.nextRoomName = name
    }

    fun itemString(): String {
        return contents.joinToString(separator = "") {"You find $it.\n"}
    }

    fun move(command: Command, world: World): String {
        val (targetName, allowed) = moves.getValue(command.noun)
        return if (allowed(world))
            targetName
        else
            roomName
    }


    private fun unknown(command: Command, world: World): String {
        world.response.say("unknown command ${command.verb}")
        return "unknown cmd ${command.verb}"
    }

    val inventory = {command:Command, world:World ->
        world.showInventory()
        roomName
    }

//    private fun inventory(command: Command, world: World): String {
//        world.showInventory()
//        return roomName
//    }

    private fun take(command: Command, world: World): String {
        val done = contents.remove(command.noun)
        if ( done ) {
            world.addToInventory(command.noun)
            world.response.say("${command.noun} taken.")
        } else {
            world.response.say("I see no ${command.noun} here!")
        }
        return roomName
    }

    private fun castSpell(command: Command, world: World): String {
        val returnRoom = when (command.noun) {
            "wd40" -> {
                world.flags.get("unlocked").set(true)
                world.response.say("The magic wd40 works! The padlock is unlocked!")
                roomName
            }
            "xyzzy" -> {
                val (targetName, allowed) = moves.getValue(command.noun)
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
