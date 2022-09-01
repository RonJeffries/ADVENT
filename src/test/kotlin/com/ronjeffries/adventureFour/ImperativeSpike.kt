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
}

class ImperativeFactory(val map:Map<String,Imperative>, val synonyms:Map<String,String> = synMap) {
    fun create(verb:String): Imperative {
        return map.getValue(synonyms.getValue(verb))
    }

    fun create(verb:String, noun:String): Imperative {
        val v = synonyms.getValue(verb)
        val n = synonyms.getValue(noun)
        val imp = map.getValue(v)
        return Imperative(imp.verb, n)
    }
}

val synMap = mapOf("e" to "east", "n" to "north").withDefault { it }

val impTable = mapOf(
    "east" to Imperative("go","east"),
    "go" to Imperative("go", "irrelevant")
    ).withDefault {
    (Imperative("none", "none"))
}
