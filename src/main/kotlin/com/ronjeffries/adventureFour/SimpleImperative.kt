package com.ronjeffries.adventureFour

class Lexicon(val synonyms: Synonyms, val verbs: Verbs, val actions: Actions) {
    fun synonym(word:String):String = synonyms.synonym(word)
    fun translate(word: String): Imperative = verbs.translate(word)
    fun act(imperative: Imperative) = actions.act(imperative)
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

data class SimpleImperative(override val verb: String, override val noun: String): Imperative {
    override lateinit var world: World
    override lateinit var room: Room
    override fun setNoun(noun: String): Imperative = SimpleImperative(verb, noun)

    override fun act(lexicon: Lexicon):String {
        lexicon.act(this)
        return said
    }

    var said: String = ""

    override fun say(s:String) {
        said = s
    }
}

data class WorldImperative(override val verb: String, override val noun: String, override val world: World, override val room: Room) :Imperative {
    override fun setNoun(noun: String): Imperative {
        return WorldImperative(verb, noun, world, room)
    }

    override fun act(lexicon: Lexicon): String {
        lexicon.act(this)
        return ""
    }

    override fun say(s: String) {
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

class Verbs(private val map:Map<String, Imperative>) {
    fun translate(verb:String): Imperative = map.getValue(verb)
}

class Synonyms(private val map: Map<String,String>) {
    fun synonym(word:String) = map.getValue(word)
}
typealias Action = (Imperative) -> Unit

class Actions(private val verbMap: MutableMap<String, Action>) {
    fun act(imperative: Imperative) {
         verbMap.getValue((imperative.verb))(imperative)
    }
}

