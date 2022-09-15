package com.ronjeffries.adventureFour

data class Imperative(
    val phrase: Phrase,
    val world: World = world(){},
    val room: Room = Room("fakeroom")
)  {
    var worldNeeded: Boolean = true
    val verb = phrase.verb!!
    val noun = phrase.noun!!

    fun say(s: String) = world.say(s)

    fun act(roomActions: Actions, worldActions: Actions) {
        worldNeeded = false
        act(roomActions)
        if (worldNeeded) act(worldActions)
    }

    fun notHandled() {
        worldNeeded = true
    }

    fun actForTesting(lexicon: Lexicon): String {
        return actForTesting(lexicon.actions)
    }

    fun actForTesting(actions: Actions): String {
        act(actions)
        return testingSaid
    }

    fun act(actions: Actions) {
        actions.act(this)
    }

    private var testingSaid: String = ""

    fun testingSay(s:String)  {
        testingSaid = s
    }

}