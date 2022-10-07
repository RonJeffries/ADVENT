package com.ronjeffries.adventureFour

typealias DescLambda = ()->String

class Room(val roomName: R, private val actions: IActions = Actions()) : IActions by actions {
    init {
        actions.add(Phrase()) { imp -> imp.notHandled() }
    }

    val contents: Items = Items()
    private val moves = mutableMapOf<D, GoTarget>().withDefault { Pair(roomName) { _: World -> true } }

    var shortDesc: DescLambda = {""}
    var longDesc: DescLambda = {""}
    var theDesc: DescLambda = {""}

    // DSL Builders

    fun desc(short: DescLambda, long: DescLambda ) {
        shortDesc = short
        longDesc = long
        theDesc = long
    }

    @Suppress("unused")
    fun desc(short: String, long: DescLambda) = desc({ short }, long)
    @Suppress("MoveLambdaOutsideParentheses", "unused")
    fun desc(short: DescLambda, long: String) = desc(short, { long })  // irritating formatting requirement
    fun desc(short: String, long: String) = desc({ short }, { long })

    fun go(direction: D, roomName: R, allowed: (World) -> Boolean = { _: World -> true }) {
        moves += direction to Pair(roomName, allowed)
    }

    // Game Play

    fun command(command: Command, world: World) {
        world.response.moveToRoomNamed(roomName)
        val phrase: Phrase = makePhrase(command, world.lexicon)
        val imp = Imperative(phrase, world, this)
        imp.act(actions, world.actions)
    }

    fun description(): String {
        return theDesc().also { setShortDesc() }
    }

    private fun makePhrase(command: Command, lexicon: Lexicon): Phrase =
        PhraseFactory(lexicon).fromString(command.input)

    fun item(thing: String, details: Item.() -> Unit = {}): Item = Item(thing).apply {
        contents.add(this)
        details()
    }

    fun setLongDesc() {
        theDesc = longDesc
    }

    fun setShortDesc() {
        theDesc = shortDesc
    }

    fun itemString(): String = contents.asFound()

    fun look() = setLongDesc()

    fun move(imperative: Imperative, world: World) {
        D.executeIfDirectionExists(imperative.noun) { direction: D ->
            val (targetName, allowed) = moves.getValue(direction)
            if (allowed(world)) world.response.moveToRoomNamed(targetName)
        }
    }

    fun take(imp: Imperative, world: World) = world.response.say(
        when (contents.moveItemTo(imp.noun, world.inventory)) {
            true -> "${imp.noun} taken."
            false -> "I see no ${imp.noun} here!"
        }
    )

    fun unknown(imperative: Imperative, world: World) = when (imperative.noun) {
        "none" -> world.response.say("I do not understand '${imperative.verb}'.")
        else -> world.response.say("I do not understand '${imperative.verb} ${imperative.noun}'.")
    }
}
