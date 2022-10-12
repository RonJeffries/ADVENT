package com.ronjeffries.adventureFour

data class Imperative(
    val phrase: Phrase,
    val world: World = world(){},
    val room: Room = Room(R.Z_FIRST))
{

    val verb = phrase.verb!!
    val noun = phrase.noun!!
    private var lookInWorld: Boolean = true

    fun act(roomActions: IActions, worldActions: IActions) {
        lookInWorld = false
        act(roomActions)
        if (lookInWorld) act(worldActions)
    }

    fun act(actions: IActions) = actions.act(this)

    fun actForTesting(actions: IActions): String {
        act(actions)
        return testingSaid
    }

    fun notHandled() {
        lookInWorld = true
    }

    fun say(s: String) = world.say(s)

    private var testingSaid: String = ""
    fun testingSay(s:String)  {
        testingSaid = s
    }

}