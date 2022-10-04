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

class Lexicon(private val synonyms: Synonyms, private val verbs: Verbs) {
    fun synonym(word:String):String = synonyms.synonym(word)
    fun translate(word: String): Phrase = verbs.translate(word)
}

class Synonyms(private val map: Map<String,String>) {
    fun synonym(word:String) = map.getValue(word)
}

class Verbs(private val map:Map<String, Phrase>) {
    fun translate(verb:String): Phrase = map.getValue(verb)
}

interface IActions {
    fun act(imperative: Imperative)
    fun action(verb: String, noun: String, action: Action)
    fun action(verb: String, action: Action)
    fun action(commands: List<String>, action: Action)
    fun add(phrase: Phrase, action: (Imperative) -> Unit)
    fun clear()
}

class Actions() : IActions {
    private val map: ActionMap = mutableMapOf<Phrase,Action>()

    override fun act(imperative: Imperative) {
        val action: (Imperative) -> Unit = find(imperative)
        action(imperative)
    }

    override fun action(verb: String, noun: String, action: Action) {
        add(Phrase(verb, noun), action)
    }

    override fun action(verb: String, action: Action) {
        add(Phrase(verb), action)
    }

    override fun action(commands: List<String>, action: Action) {
        commands.forEach { makeAction(it, action) }
    }

    override fun add(phrase: Phrase, action: (Imperative) -> Unit) {
        map[phrase] = action
    }

    override fun clear() {
        map.clear()
    }

    fun makeAction(command:String, action: Action) {
        val words = command.lowercase().split(" ")
        when (words.size) {
            1 -> action(words[0], action)
            else -> action(words[0], words[1], action)
        }
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

