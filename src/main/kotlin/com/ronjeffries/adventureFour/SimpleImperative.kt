package com.ronjeffries.adventureFour

class Lexicon(val synonyms: Synonyms, val verbs: Verbs, val actions: Actions) {
    fun synonym(word:String):String = synonyms.synonym(word)
    fun translate(word: String): WorldImperative = verbs.translate(word)
    fun act(imperative: WorldImperative) = actions.act(imperative)
}

data class WorldImperative(
    val verb: String,
    val noun: String,
    val world: World,
    val room: Room
)  {
    fun setNoun(noun: String): WorldImperative {
        return WorldImperative(verb, noun, world, room)
    }

    fun act(lexicon: Lexicon): String {
        lexicon.act(this)
        return said
    }

    var said: String = ""

    fun say(s:String) {
        said = s
    }

}

class ImperativeFactory(val lexicon: Lexicon) {

    fun fromOneWord(verb:String): WorldImperative = imperative(verb)
    fun fromTwoWords(verb:String, noun:String): WorldImperative = imperative(verb).setNoun(synonym(noun))
    private fun imperative(verb: String) = lexicon.translate(synonym(verb))
    private fun synonym(verb: String) = lexicon.synonym(verb)

    fun fromString(input: String): WorldImperative {
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

