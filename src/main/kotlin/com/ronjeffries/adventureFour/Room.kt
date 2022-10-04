package com.ronjeffries.adventureFour

class Room(val roomName:R) {
    val contents: Items = Items()
    private val moves = mutableMapOf<D, GoTarget>().withDefault { Pair(roomName) { _: World -> true } }

    //    private val actionMap = mutableMapOf<Phrase,Action>(
//        Phrase() to {imp -> imp.notHandled()}
//    )
    private val actions = Actions()

    init {
        actions.add(Phrase()) { imp -> imp.notHandled() }
    }

    var shortDesc = ""
    var longDesc = ""
    var theDesc = ""

    // DSL Builders

    fun action(verb: String, noun: String, action: Action) {
        actions.add(Phrase(verb, noun), action)
    }

    fun action(verb: String, action: Action) {
        actions.add(Phrase(verb), action)
    }

    fun action(actions: List<String>, action: Action) {
        actions.forEach { makeAction(it, action) }
    }

    fun makeAction(command:String, action: Action) {
        val words = command.lowercase().split(" ")
        when (words.size) {
            1 -> action(words[0], action)
            else -> action(words[0], words[1], action)
        }
    }

    fun desc(short: String, long: String) {
        shortDesc = short
        longDesc = long
        theDesc = long
    }

    fun go(direction: D, roomName: R, allowed: (World) -> Boolean = { _: World -> true }) {
        moves += direction to Pair(roomName, allowed)
    }

    // Game Play

    fun command(command: Command, world: World) {
        world.response.nextRoomName = roomName
        val phrase: Phrase = makePhrase(command, world.lexicon)
        val imp = Imperative(phrase, world, this)
        imp.act(actions, world.actions)
    }

    fun description(): String {
        return theDesc.also { setShortDesc() }
    }

    private fun makePhrase(command: Command, lexicon: Lexicon): Phrase =
        PhraseFactory(lexicon).fromString(command.input)

    fun item(thing: String, details: Item.() -> Unit = {}): Item {
        val item = Item(thing)
        contents.add(item)
        item.details()
        return item
    }

    fun setLongDesc() {
        theDesc = longDesc
    }

    fun setShortDesc() {
        theDesc = shortDesc
    }

    fun itemString(): String = contents.asFound()

    fun look() {
        setLongDesc()
    }

    fun move(imperative: Imperative, world: World) {
        D.executeIfDirectionExists(imperative.noun) { direction: D ->
            val (targetName, allowed) = moves.getValue(direction)
            if (allowed(world)) world.response.nextRoomName = targetName
        }
    }

    fun take(imp: Imperative, world: World) {
        with(world) {
            imp.noun.let {
                response.say(
                    when (contents.moveItemTo(it, inventory)) {
                        true -> "$it taken."
                        false -> "I see no $it here!"
                    }
                )
            }
        }
    }

    fun unknown(imperative: Imperative, world: World) {
        when (imperative.noun) {
            "none" -> world.response.say("I do not understand '${imperative.verb}'.")
            else -> world.response.say("I do not understand '${imperative.verb} ${imperative.noun}'.")
        }
    }
}
