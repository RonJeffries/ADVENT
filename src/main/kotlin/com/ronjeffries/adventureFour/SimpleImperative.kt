package com.ronjeffries.adventureFour

class Lexicon(val synonyms: Synonyms, val verbs: Verbs, val actions: Actions) {
    fun synonym(word:String):String = synonyms.synonym(word)
    fun translate(word: String): Imperative = verbs.translate(word)
    fun act(imperative: Imperative) = actions.act(imperative)
    fun defineLocalActions(newActions: MutableMap<String, (Imperative) -> Unit>) {
        actions.defineLocalActions(newActions)
    }
}

data class Imperative(
    val verb: String,
    val noun: String,
    val world: World,
    val room: Room )  {

    fun say(s: String) = world.say(s)

    fun setNoun(noun: String): Imperative {
        return Imperative(verb, noun, world, room)
    }

    fun act(lexicon: Lexicon): String {
        lexicon.act(this)
        return testingSaid
    }

    var testingSaid: String = ""

    fun testingSay(s:String) {
        testingSaid = s
    }

}

class ImperativeFactory(val lexicon: Lexicon) {

    fun fromOneWord(verb:String): Imperative = imperative(verb)
    fun fromTwoWords(verb:String, noun:String): Imperative
        = imperative(verb).setNoun(synonym(noun))
    fun imperative(verb: String) = lexicon.translate(synonym(verb))
    fun synonym(verb: String) = lexicon.synonym(verb)

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

class Actions(map: MutableMap<String, Action>) {
    private val verbMap = SmartMap(map)
    fun act(imperative: Imperative) {
         verbMap.getValue((imperative.verb))(imperative)
    }

    fun putGlobal(action: Pair<String, (Imperative) -> Unit>) {
        verbMap.putGlobal(action.first, action.second)
    }

    fun clear() {
        verbMap.clearLocal()
    }

    fun defineLocalActions(actions: MutableMap<String, (Imperative) -> Unit>) {
        clear()
        putAllLocal(actions)
    }

    fun putAllLocal(actions: MutableMap<String, (Imperative) -> Unit>) {
        verbMap.putAllLocal(actions)
    }

}

