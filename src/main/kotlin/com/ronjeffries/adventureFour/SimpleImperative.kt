package com.ronjeffries.adventureFour

class Lexicon(val synonyms: Synonyms, val verbs: Verbs, val actions: Actions) {
    fun synonym(word:String):String = synonyms.synonym(word)
    fun translate(word: String): WorldImperative = verbs.translate(word)
    fun act(imperative: WorldImperative) = actions.act(imperative)
}

interface Imperative {
    fun setNoun(noun: String): Imperative
    fun act(lexicon: Lexicon): String
    fun say(s: String)

    val verb: String
    val noun: String
    val world: World
    val room: Room
}

data class WorldImperative(override val verb: String, override val noun: String, override val world: World, override val room: Room) :Imperative {
    override fun setNoun(noun: String): Imperative {
        return WorldImperative(verb, noun, world, room)
    }

    override fun act(lexicon: Lexicon): String {
        lexicon.act(this)
        return said
    }

    var said: String = ""

    override fun say(s:String) {
        said = s
    }

}

class ImperativeFactory(val lexicon: Lexicon) {

    fun fromOneWord(verb:String): Imperative = imperative(verb)
    fun fromTwoWords(verb:String, noun:String): Imperative = imperative(verb).setNoun(synonym(noun))
    private fun imperative(verb: String) = lexicon.translate(synonym(verb))
    private fun synonym(verb: String) = lexicon.synonym(verb)

    fun fromString(input: String): Imperative {
        val words = input.lowercase().split(" ")
        return when (words.size) {
            1-> fromOneWord(words[0])
            2-> fromTwoWords(words[0], words[1])
            else -> fromTwoWords(":tooManyWords", input)
        }
    }
}

class Verbs(private val map:Map<String, WorldImperative>) {
    fun translate(verb:String): WorldImperative = map.getValue(verb)
}

class Synonyms(private val map: Map<String,String>) {
    fun synonym(word:String) = map.getValue(word)
}
typealias Action = (WorldImperative) -> Unit

class Actions(private val verbMap: MutableMap<String, Action>) {
    fun act(imperative: WorldImperative) {
         verbMap.getValue((imperative.verb))(imperative)
    }
}

