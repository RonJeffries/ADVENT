package com.ronjeffries.adventureFour



data class Phrase(val verb: String?=null, val noun: String?=null) {
    fun asVerb() = Phrase(this.verb)
    fun asNoun() = Phrase(noun=this.noun)
    fun asEmpty() = Phrase()
    fun asImperative() = Imperative(this)
    fun setNoun(noun: String): Phrase {
        return Phrase(this.verb,noun)
    }
}

typealias Action = (Imperative) -> Unit
typealias ActionMap = MutableMap<Phrase, Action>

class Lexicon(private val synonyms: Synonyms, private val verbs: Verbs, val actions: Actions) {
    fun synonym(word:String):String = synonyms.synonym(word)
    fun translate(word: String): Phrase = verbs.translate(word)
}

class Synonyms(private val map: Map<String,String>) {
    fun synonym(word:String) = map.getValue(word)
}

class Verbs(private val map:Map<String, Phrase>) {
    fun translate(verb:String): Phrase = map.getValue(verb)
}

class Actions(val map: ActionMap) {
    fun act(imperative: Imperative) {
        val action: (Imperative) -> Unit = find(imperative)
        action(imperative)
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

