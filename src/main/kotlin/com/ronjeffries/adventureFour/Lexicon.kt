package com.ronjeffries.adventureFour

typealias Action = (Imperative) -> Unit
typealias ActionMap = MutableMap<String, Action>

class Lexicon(private val synonyms: Synonyms, private val verbs: Verbs, val actions: Actions) {
    fun synonym(word:String):String = synonyms.synonym(word)
    fun translate(word: String): Imperative = verbs.translate(word)
    fun act(imperative: Imperative) = actions.act(imperative)
    fun defineLocalActions(newActions: ActionMap) = actions.defineLocalActions(newActions)
}

class Synonyms(private val map: Map<String,String>) {
    fun synonym(word:String) = map.getValue(word)
}

class Verbs(private val map:Map<String, Imperative>) {
    fun translate(verb:String): Imperative = map.getValue(verb)
}

class Actions(map: ActionMap) {
    private val verbMap = SmartMap(map)

    fun act(imperative: Imperative) = verbMap.getValue((imperative.verb))(imperative)

    fun defineLocalActions(actions: ActionMap) {
        verbMap.clearLocal()
        verbMap.putAllLocal(actions)
    }

    fun putGlobal(action: Pair<String, Action>) = verbMap.putGlobal(action.first, action.second)
}

