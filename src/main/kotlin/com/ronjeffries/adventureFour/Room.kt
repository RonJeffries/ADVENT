package com.ronjeffries.adventureFour

class Room(val roomName:R) {
    val contents: Items = Items()
    private val moves = mutableMapOf<D,GoTarget>().withDefault { Pair(roomName) { _: World -> true } }
    private val actionMap = mutableMapOf<Phrase,Action>(
        Phrase() to {imp -> imp.notHandled()}
    )
    private val actions = Actions(actionMap)
    var shortDesc = ""
    var longDesc = ""
    var theDesc = ""

    // DSL Builders

    fun action(verb: String, noun: String, action: Action) {
        action(Phrase(verb,noun), action)
    }

    fun action(phrase: Phrase, action: Action) {
        actionMap[phrase] = action
    }

    fun desc(short: String, long: String) {
        shortDesc = short
        longDesc = long
        theDesc = long
    }

    fun go(direction: D, roomName: R, allowed: (World)->Boolean = { _:World -> true}){
        moves += direction to Pair(roomName, allowed)
    }

    // Game Play

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

    fun command(command: Command, world: World) {
        world.response.nextRoomName = roomName
        val phrase: Phrase = makePhrase(command, world.lexicon)
        val imp = Imperative(phrase,world, this)
        imp.act(actions, world.actions)
    }

    fun description(): String {
        return theDesc.also {setShortDesc()}
    }

    private fun makePhrase(command: Command, lexicon: Lexicon): Phrase
        = PhraseFactory(lexicon).fromString(command.input)

    fun item(thing: String, details: Item.()->Unit = {}): Item {
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
        val (targetName, allowed) = moves.getValue(D.valueMatching(imperative.noun))
        if (allowed(world)) world.response.nextRoomName = targetName
    }

    fun take(imp: Imperative, world: World) {
        with (world) { imp.noun.let {
        response.say(
            when (contents.moveItemTo(it, inventory)) {
                true -> "$it taken."
                false -> "I see no $it here!" }) }}
    }

    fun unknown(imperative: Imperative, world: World ) {
        world.response.say("I do not understand '${imperative.verb} ${imperative.noun}'.")
    }

    // Utilities and Other

    val roomReferences: Set<R> get () = moves.values.map {it.first}.toSet()
}
