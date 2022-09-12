package com.ronjeffries.adventureFour



data class Phrase(val verb: String?=null, val noun: String?=null) {
    fun asVerb() = Phrase(this.verb)
    fun asNoun() = Phrase(noun=this.noun)
    fun asEmpty() = Phrase()
}

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

class Actions(val map: ActionMap) {
    fun act(imperative: Imperative) {
        val imp: (Imperative) -> Unit = find(imperative)
        imp(imperative)
    }

    private fun find(imperative: Imperative): Action {
        val p = Phrase(imperative.verb, imperative.noun)
        return map.getOrElse(p) {
            map.getOrElse(p.asVerb()) {
                map.getOrElse(p.asNoun()) {
                    map.getValue(Phrase()) }
            }
        }
    }
}

