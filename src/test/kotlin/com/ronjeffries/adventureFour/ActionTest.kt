package com.ronjeffries.adventureFour

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SmartMap<K,V>(val global: MutableMap<K,V>, val local: MutableMap<K,V>) {
    fun getValue(k:K): V = local.getValue(k)
    fun getGlobalValue(k: K): V = global.getValue(k)
}

class ActionTest {
    @Test
    fun `table defaulting to table`() {
        val g = mutableMapOf<String,String>(
            "go" to "went"
        ).withDefault { key -> "I have no idea what $key is" }
        val l = mutableMapOf(
            "say" to "said"
        ).withDefault { key: String -> g.getValue(key) }
        assertThat(l.getValue("say")).isEqualTo("said")
        assertThat(l.getValue("go")).isEqualTo("went")
        assertThat(l.getValue("whazzup")).isEqualTo("I have no idea what whazzup is")
    }

    @Test
    fun `smart map`() {
        val g = mutableMapOf<String,String>(
            "go" to "went",
            "say" to "global said"
        ).withDefault { key -> "I have no idea what $key is" }
        val l = mutableMapOf(
            "say" to "said"
        ).withDefault { key: String -> g.getValue(key) }
        val sm: SmartMap<String, String> = SmartMap(g,l)
        assertThat(sm.getValue("say")).isEqualTo("said")
        assertThat(sm.getValue("go")).isEqualTo("went")
        assertThat(sm.getValue("whazzup")).isEqualTo("I have no idea what whazzup is")
        assertThat(sm.getGlobalValue("say")).isEqualTo("global said")
    }
}