package com.ronjeffries.adventureFour

class Lexicon(val synonyms: Synonyms, val verbs: Verbs, val actions: Actions) {
    fun synonym(word:String):String = synonyms.synonym(word)
    fun translate(word: String): Imperative = verbs.translate((word))
    fun act(imperative: Imperative) = actions.act(imperative)
}

data class Imperative(val verb: String, val noun: String) {
    fun setNoun(noun: String): Imperative = Imperative(verb, noun)

    fun act(lexicon: Lexicon):String {
        lexicon.act(this)
        return said
    }
    var said: String = ""

    fun say(s:String) {
        said = s
    }
}

class ImperativeFactory(private val verbs: Verbs, private val synonyms: Synonyms) {

    fun fromOneWord(verb:String): Imperative = imperative(verb)
    fun fromTwoWords(verb:String, noun:String) = imperative(verb).setNoun(synonym(noun))
    private fun imperative(verb: String) = verbs.translate(synonym(verb))
    private fun synonym(verb: String) = synonyms.synonym(verb)

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

class Actions(private val verbMap:Map<String, Action> = TestActionTable) {
    fun act(imperative: Imperative) {
         verbMap.getValue((imperative.verb))(imperative)
    }
}

val TestActionTable = mapOf(
    "go" to { imp: Imperative -> imp.say("went ${imp.noun}")},
    "say" to { imp: Imperative -> imp.say("said ${imp.noun}")},
    "inventory" to { imp: Imperative -> imp.say("You got nothing")}
).withDefault {it ->{imp: Imperative -> imp.say("I can't ${imp.verb} a ${imp.noun}") }}

val TestVerbTable = mapOf(
    "go" to Imperative("go", "irrelevant"),
    "east" to Imperative("go", "east"),
    "west" to Imperative("go", "west"),
    "north" to Imperative("go", "north"),
    "south" to Imperative("go", "south"),
    "say" to Imperative("say", "irrelevant"),
    "xyzzy" to Imperative("say", "xyzzy"),
    ).withDefault { (Imperative(it, "none"))
}