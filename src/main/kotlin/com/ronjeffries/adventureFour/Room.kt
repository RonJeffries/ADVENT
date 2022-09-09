package com.ronjeffries.adventureFour


typealias GoTarget = Pair<String, (World)->Boolean>

class Room(val roomName: String) {
    val contents = mutableSetOf<String>()
    private val moves = mutableMapOf<String,GoTarget>().withDefault { Pair(roomName) { _: World -> true } }
    val actions = mutableMapOf<String,Action>()
    var shortDesc = ""
    var longDesc = ""

    // DSL Builders

    fun action(verb: String, action: Action) {
        actions[verb] = action
    }

    fun desc(short: String, long: String) {
        shortDesc = short
        longDesc = long
    }
    fun go(direction: String, roomName: String, allowed: (World)->Boolean = { _:World -> true}) {
        moves += direction to Pair(roomName, allowed)
    }

    // Game Play

    fun command(command: Command, world: World) {
        world.response.nextRoomName = roomName
        world.defineLocalActions(actions)
        val factory = ImperativeFactory(world.lexicon)
        val imperative = factory.fromString(command.input)
        world.testVerb = imperative.verb
        world.testNoun = imperative.noun
        val imp = Imperative(imperative.verb,imperative.noun,world, this)
        imp.act(world.lexicon)
    }

    fun itemString(): String {
        return contents.joinToString(separator = "") {"You find $it.\n"}
    }

    val castSpell = { imperative: Imperative, world: World ->
        when (imperative.noun) {
            "wd40" -> {
                world.flags.get("unlocked").set(true)
                world.response.say("The magic wd40 works! The padlock is unlocked!")
            }
            "xyzzy" -> { move(imperative, world) }
            else -> { world.response.say("Nothing happens here.") }
        }
    }

    val move = { imperative: Imperative, world: World ->
        val (targetName, allowed) = moves.getValue(imperative.noun)
        if (allowed(world)) world.response.nextRoomName = targetName
    }

    val take = { imperative: Imperative, world: World ->
        val done = contents.remove(imperative.noun)
        if ( done ) {
            world.addToInventory(imperative.noun)
            world.response.say("${imperative.noun} taken.")
        } else {
            world.response.say("I see no ${imperative.noun} here!")
        }
    }

    val unknown = { imperative: Imperative, world: World ->
        println("unknown")
        world.response.say("unknown command '${imperative.verb} ${imperative.noun}'")
    }

    fun item(thing: String) {
        contents+=thing
    }

    // Utilities and Other

    val roomReferences: Set<String> get () {
        return moves.values.map {it.first}.toSet()
    }
}
