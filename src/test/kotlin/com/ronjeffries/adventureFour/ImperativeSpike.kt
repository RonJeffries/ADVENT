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
        val imperatives = ImperativeMap(impTable)
        var imp: Imperative = imperatives.getValue("east")
        assertThat(imp).isEqualTo(Imperative("go","east"))
        imp = imperatives.getValue("e")
        assertThat(imp).isEqualTo(Imperative("go","east"))
    }

    @Test
    fun `imperative can act`() {
        val imperatives = ImperativeMap(impTable)
        var imp: Imperative = imperatives.getValue("east")
        assertThat(imp.act()).isEqualTo("went east")
        imp = imperatives.getValue("e")
        assertThat(imp.act()).isEqualTo("went east")
        imp = Imperative("forge", "sword")
        assertThat(imp.act()).isEqualTo("I can't forge a sword")
    }
}

class ImperativeMap(val map:Map<String,Imperative>, val synonyms:Map<String,String> = synMap) {
    fun getValue(verb:String, noun:String = ""): Imperative {
        return map.getValue(synonyms.getValue(verb))
    }
}

val synMap = mapOf("e" to "east", "n" to "north").withDefault { it }

val impTable = mapOf("east" to Imperative("go","east")).withDefault {
    (Imperative("none", "none"))
}
