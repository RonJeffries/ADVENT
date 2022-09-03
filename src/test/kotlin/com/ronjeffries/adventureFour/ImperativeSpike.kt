package com.ronjeffries.adventureFour

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class ImperativeSpike {
    @Test
    fun `create Imperative`() {
        val imp = Imperative("go", "east")
        assertThat(imp.verb).isEqualTo("go")
        assertThat(imp.noun).isEqualTo("east")
    }

    @Test
    fun `look up some imperatives`() {
        val imperatives = ImperativeFactory(Verbs(verbTable))
        var imp: Imperative = imperatives.create("east")
        assertThat(imp).isEqualTo(Imperative("go","east"))
        imp = imperatives.create("e")
        assertThat(imp).isEqualTo(Imperative("go","east"))
    }

    @Test
    fun `imperative can act`() {
        val imperatives = ImperativeFactory(Verbs(verbTable))
        var imp: Imperative = imperatives.create("east")
        assertThat(imp.act()).isEqualTo("went east")
        imp = imperatives.create("e")
        assertThat(imp.act()).isEqualTo("went east")
        imp = Imperative("forge", "sword")
        assertThat(imp.act()).isEqualTo("I can't forge a sword")
    }

    @Test
    fun `two word legal command`() {
        val imperatives = ImperativeFactory(verbTable)
        val imp = imperatives.create("go", "e")
        assertThat(imp.act()).isEqualTo("went east")
    }

    @Test
    fun `one and two word commands`() {
        val imperatives = ImperativeFactory(verbTable)
        var imp = imperatives.create("go", "w")
        assertThat(imp.act()).isEqualTo("went west")
        imp = imperatives.create("w")
        assertThat(imp.act()).isEqualTo("went west")
        imp = imperatives.create("xyzzy")
        assertThat(imp.act()).isEqualTo("said xyzzy")
        imp = imperatives.create("say", "xyzzy")
        assertThat(imp.act()).isEqualTo("said xyzzy")
        imp = imperatives.create("say", "unknownWord")
        assertThat(imp.act()).isEqualTo("said unknownWord")
        imp = imperatives.create("unknownVerb", "unknownNoun")
        assertThat(imp.act()).isEqualTo("I can't unknownVerb a unknownNoun")
        imp = imperatives.create("unknownWord")
        assertThat(imp.act()).isEqualTo("I can't unknownWord a none")
        imp = imperatives.create("inventory")
        assertThat(imp.act()).isEqualTo("You got nothing")
    }

    @Test
    fun verbTranslator() {
        val vt = Verbs(verbTable)
        val imp = vt.translate("east")
        assertThat(imp).isEqualTo(Imperative("go", "east"))
    }

    @Test
    fun `create a lexicon`() {
        val synonyms = Synonyms(synonymTable)
        val verbs = Verbs(verbTable)
        val actions = Actions(actionTable)
        val lexicon = Lexicon(synonyms, verbs, actions)
        assertThat(lexicon.synonym("e")).isEqualTo("east")
    }
}

class Lexicon(val synonyms: Synonyms, val verbs: Verbs, val actions: Actions) {
    fun synonym(word:String):String = synonyms.synonym(word)

}


data class Imperative(val verb: String, val noun: String) {
    var said: String = ""
    val actions = Actions()
    fun act():String {
        actions.performAction(this)
        return said
    }

    fun say(s:String) {
        said = s
    }

    fun setNoun(noun: String): Imperative = Imperative(verb,noun)
}

class ImperativeFactory(private val verbs:Verbs, private val synonyms:Synonyms = Synonyms(synonymTable)) {
    constructor(map: Map<String, Imperative>) : this(Verbs(map))

    fun create(verb:String): Imperative = imperative(verb)
    fun create(verb:String, noun:String) = imperative(verb).setNoun(synonym(noun))
    private fun imperative(verb: String) = verbs.translate(synonym(verb))
    private fun synonym(verb: String) = synonyms.synonym(verb)
}

class Verbs(private val map:Map<String,Imperative>) {
    fun translate(verb:String): Imperative = map.getValue(verb)
}

class Synonyms(private val map: Map<String,String>) {
    fun synonym(word:String) = map.getValue(word)
}

typealias Action = (Imperative) -> Unit

class Actions(private val verbMap:Map<String, Action> = actionTable) {
    fun performAction(imperative:Imperative) {
         verbMap.getValue((imperative.verb))(imperative)
    }
}

// language control tables. (prototypes)

val actionTable = mapOf(
    "go" to { imp:Imperative -> imp.say("went ${imp.noun}")},
    "say" to { imp:Imperative -> imp.say("said ${imp.noun}")},
    "inventory" to { imp:Imperative -> imp.say("You got nothing")}
).withDefault {it ->{imp:Imperative -> imp.say("I can't ${imp.verb} a ${imp.noun}") }}

val synonymTable = mapOf(
    "e" to "east",
    "n" to "north",
    "w" to "west",
    "s" to "south").withDefault { it }

val verbTable = mapOf(
    "go" to Imperative("go", "irrelevant"),
    "east" to Imperative("go","east"),
    "west" to Imperative("go","west"),
    "north" to Imperative("go","north"),
    "south" to Imperative("go","south"),
    "say" to Imperative("say", "irrelevant"),
    "xyzzy" to Imperative("say", "xyzzy"),
    ).withDefault { (Imperative(it, "none"))
}
