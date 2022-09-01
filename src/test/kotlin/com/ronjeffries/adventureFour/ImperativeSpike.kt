package com.ronjeffries.adventureFour

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

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

class ImperativeSpike {
    @Test
    fun `create Imperative`() {
        val imp = Imperative("go", "east")
        assertThat(imp.verb).isEqualTo("go")
        assertThat(imp.noun).isEqualTo("east")
    }

    @Test
    fun `look up some imperatives`() {
        val imperatives = ImperativeFactory(impTable)
        var imp: Imperative = imperatives.create("east")
        assertThat(imp).isEqualTo(Imperative("go","east"))
        imp = imperatives.create("e")
        assertThat(imp).isEqualTo(Imperative("go","east"))
    }

    @Test
    fun `imperative can act`() {
        val imperatives = ImperativeFactory(impTable)
        var imp: Imperative = imperatives.create("east")
        assertThat(imp.act()).isEqualTo("went east")
        imp = imperatives.create("e")
        assertThat(imp.act()).isEqualTo("went east")
        imp = Imperative("forge", "sword")
        assertThat(imp.act()).isEqualTo("I can't forge a sword")
    }

    @Test
    fun `two word legal command`() {
        val imperatives = ImperativeFactory(impTable)
        var imp = imperatives.create("go", "e")
        assertThat(imp.act()).isEqualTo("went east")
    }

    @Test
    fun `verbTranslator`() {
        val vt = VerbTranslator(impTable)
        val imp = vt.translate("east")
        assertThat(imp).isEqualTo(Imperative("go", "east"))
    }
}

class VerbTranslator(val map:Map<String,Imperative>) {
    fun translate(verb:String): Imperative {
        return map.getValue(verb)
    }
}

class ImperativeFactory(val verbTranslator:Map<String,Imperative>, val synonyms:Map<String,String> = synMap) {
    fun create(verb:String): Imperative = imperative(verb)
    fun create(verb:String, noun:String) = imperative(verb).setNoun(synonym(noun))
    private fun imperative(verb: String) = verbTranslator.getValue(synonym(verb))
    private fun synonym(verb: String) = synonyms.getValue(verb)
}

val synMap = mapOf("e" to "east", "n" to "north").withDefault { it }

val impTable = mapOf(
    "east" to Imperative("go","east"),
    "go" to Imperative("go", "irrelevant")
    ).withDefault {
    (Imperative("none", "none"))
}
