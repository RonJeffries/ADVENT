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
            "take" -> take
            "go" -> move
            "say" -> castSpell
            else -> unknown
        }
        val name:String = action(command, world)
        world.response.nextRoomName = name
    }

    fun itemString(): String {
        return contents.joinToString(separator = "") {"You find $it.\n"}
    }

    val move = {command: Command, world: World ->
        val (targetName, allowed) = moves.getValue(command.noun)
        if (allowed(world))
            targetName
        else
            roomName
    }

    val unknown = {command: Command, world: World ->
        println("Arrived in unknown")
        world.response.say("unknown command '${command.verb} ${command.noun}'")
        roomName
    }

    val inventory = {command:Command, world:World ->
        world.showInventory()
        roomName
    }

    val take = {command: Command, world: World ->
        val done = contents.remove(command.noun)
        if ( done ) {
            world.addToInventory(command.noun)
            world.response.say("${command.noun} taken.")
        } else {
            world.response.say("I see no ${command.noun} here!")
        }
        roomName
    }

    val castSpell = {command: Command, world: World ->
        val returnRoom = when (command.noun) {
            "wd40" -> {
                world.flags.get("unlocked").set(true)
                world.response.say("The magic wd40 works! The padlock is unlocked!")
                roomName
            }
            "xyzzy" -> {
                val (targetName, allowed) = moves.getValue(command.noun)
                if (allowed(world))
                    targetName
                else
                    roomName
            }
            else -> {
                world.response.say("Nothing happens here.")
                roomName
            }
        }
        returnRoom
    }

    fun item(thing: String) {
        contents+=thing
    }

    // Utilities and Other

    val roomReferences: Set<String> get () {
        return moves.values.map {it.first}.toSet()
    }
}
