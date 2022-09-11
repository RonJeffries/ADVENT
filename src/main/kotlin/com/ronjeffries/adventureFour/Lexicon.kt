package com.ronjeffries.adventureFour



data class Phrase(val verb: String?=null, val noun: String?=null)
typealias Action = (Imperative) -> Unit
typealias ActionMap = MutableMap<Phrase, Action>

class Lexicon(private val synonyms: Synonyms, private val verbs: Verbs, val actions: Actions) {
    fun synonym(word:String):String = synonyms.synonym(word)
    fun translate(word: String): Imperative = verbs.translate(word)
    fun act(imperative: Imperative) = actions.act(imperative)
}

class Synonyms(private val map: Map<String,String>) {
    fun synonym(word:String) = map.getValue(word)
}

class Verbs(private val map:Map<String, Imperative>) {
    fun translate(verb:String): Imperative = map.getValue(verb)
}

class Actions(map: ActionMap) {
    private val verbMap = SmartMap(map)

    fun act(imperative: Imperative) = verbMap.getValue(Phrase(imperative.verb))(imperative)

    fun putGlobal(action: Pair<String, Action>) = verbMap.putGlobal(Phrase(action.first), action.second)
}

