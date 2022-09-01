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
        val imperatives = ImperativeFactory(VerbTranslator(imperativeTable))
        var imp: Imperative = imperatives.create("east")
        assertThat(imp).isEqualTo(Imperative("go","east"))
        imp = imperatives.create("e")
        assertThat(imp).isEqualTo(Imperative("go","east"))
    }

    @Test
    fun `imperative can act`() {
        val imperatives = ImperativeFactory(VerbTranslator(imperativeTable))
        var imp: Imperative = imperatives.create("east")
        assertThat(imp.act()).isEqualTo("went east")
        imp = imperatives.create("e")
        assertThat(imp.act()).isEqualTo("went east")
        imp = Imperative("forge", "sword")
        assertThat(imp.act()).isEqualTo("I can't forge a sword")
    }

    @Test
    fun `two word legal command`() {
        val imperatives = ImperativeFactory(imperativeTable)
        var imp = imperatives.create("go", "e")
        assertThat(imp.act()).isEqualTo("went east")
    }

    @Test
    fun `verbTranslator`() {
        val vt = VerbTranslator(imperativeTable)
        val imp = vt.translate("east")
        assertThat(imp).isEqualTo(Imperative("go", "east"))
    }
}

data class Imperative(val verb: String, val noun: String) {
    fun act():String {
        val action:(Imperative) -> String = when(verb) {
            "go" -> { i -> "went $noun" }
            else -> { i -> "I can't $verb a $noun"}
        }
        return action(this)
    }

    fun setNoun(noun: String): Imperative {
        return Imperative(verb,noun)
    }
}

class ImperativeFactory(val verbTranslator:VerbTranslator, val synonyms:Synonyms = Synonyms(synonymTable)) {
    constructor(map: Map<String, Imperative>) : this(VerbTranslator(map))

    fun create(verb:String): Imperative = imperative(verb)
    fun create(verb:String, noun:String) = imperative(verb).setNoun(synonym(noun))
    private fun imperative(verb: String) = verbTranslator.translate(synonym(verb))
    private fun synonym(verb: String) = synonyms.synonym(verb)
}

class VerbTranslator(val map:Map<String,Imperative>) {
    fun translate(verb:String): Imperative = map.getValue(verb)
}

class Synonyms(val map: Map<String,String>) {
    fun synonym(word:String) = map.getValue(word)
}

// language control tables. (prototypes)

val synonymTable = mapOf("e" to "east", "n" to "north").withDefault { it }

val imperativeTable = mapOf(
    "east" to Imperative("go","east"),
    "go" to Imperative("go", "irrelevant")
    ).withDefault { (Imperative("none", "none"))
}
