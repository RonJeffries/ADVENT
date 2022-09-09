package com.ronjeffries.adventureFour

data class Imperative(
    val verb: String,
    val noun: String,
    val world: World,
    val room: Room
)  {

    fun say(s: String) = world.say(s)

    fun setNoun(noun: String): Imperative {
        return Imperative(verb, noun, world, room)
    }

    fun act(lexicon: Lexicon): String {
        lexicon.act(this)
        return testingSaid
    }

    private var testingSaid: String = ""

    fun testingSay(s:String)  {
        testingSaid = s
    }

}