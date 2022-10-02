package com.ronjeffries.adventureFour

data class Imperative(
    val phrase: Phrase,
    val world: World = world(){},
    val room: Room = Room(R.Z_FIRST)) {
    var worldNeeded: Boolean = true
    val verb = phrase.verb!!
    val noun = phrase.noun!!

    fun act(roomActions: Actions, worldActions: Actions) {
        worldNeeded = false
        act(roomActions)
        if (worldNeeded) act(worldActions)
    }

    fun act(actions: Actions) {
        actions.act(this)
    }

    fun actForTesting(actions: Actions): String {
        act(actions)
        return testingSaid
    }

    fun notHandled() {
        worldNeeded = true
    }

    fun say(s: String) = world.say(s)

    private var testingSaid: String = ""

    fun testingSay(s:String)  {
        testingSaid = s
    }

}