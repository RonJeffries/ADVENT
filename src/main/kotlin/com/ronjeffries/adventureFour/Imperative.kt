package com.ronjeffries.adventureFour

data class Imperative(
    val phrase: Phrase,
    val world: World,
    val room: Room
)  {
    val verb = phrase.verb!!
    val noun = phrase.noun!!

    fun say(s: String) = world.say(s)

    fun setNoun(noun: String): Imperative {
        return Imperative(Phrase(phrase.verb, noun), world, room)
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