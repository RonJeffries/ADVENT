package com.ronjeffries.adventureFour


typealias GoTarget = Pair<String, (World)->Boolean>

class Room(val roomName: String) {
    val contents: Items = Items()
    private val moves = mutableMapOf<String,GoTarget>().withDefault { Pair(roomName) { _: World -> true } }
    private val actionMap = mutableMapOf<Phrase,Action>(
        Phrase() to {imp -> imp.notHandled()}
    )
    private val actions = Actions(actionMap)
    var shortDesc = ""
    var longDesc = ""

    // DSL Builders

    fun action(phrase: Phrase, action: Action) {
        actionMap[phrase] = action
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
        val phrase: Phrase = makePhrase(command, world.lexicon)
        val imp = Imperative(phrase,world, this)
        imp.act(actions, world.actions)
    }

    private fun makePhrase(command: Command, lexicon: Lexicon): Phrase {
        val factory = PhraseFactory(lexicon)
        return factory.fromString(command.input)
    }

    fun itemString(): String {
        return contents.asFound()
    }

    fun castSpell(imperative: Imperative, world: World) {
        when (imperative.noun) {
            "wd40" -> {
                world.flags.get("unlocked").set(true)
                world.response.say("The magic wd40 works! The padlock is unlocked!")
            }
            "xyzzy" -> { move(imperative, world) }
            else -> { world.response.say("Nothing happens here.") }
        }
    }

    fun move(imperative: Imperative, world: World) {
        val (targetName, allowed) = moves.getValue(imperative.noun)
        if (allowed(world)) world.response.nextRoomName = targetName
    }

    fun take(imp: Imperative, world: World) {
        with(world) {
            val message = imp.noun.let {
                when (contents.moveItemTo(it, inventory)) {
                    true -> "$it taken."
                    false -> "I see no $it here!"
                }
            }
            response.say(message)
        }
    }

    fun unknown(imperative: Imperative, world: World ) {
        println("unknown")
        world.response.say("unknown command '${imperative.verb} ${imperative.noun}'")
    }

    fun item(thing: String, details: Item.()->Unit): Item {
        val item = Item(thing)
        contents.add(item)
        item.details()
        return item
    }

    // Utilities and Other

    val roomReferences: Set<String> get () {
        return moves.values.map {it.first}.toSet()
    }
}
