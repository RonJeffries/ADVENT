package com.ronjeffries.adventureFour

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SmartMap<K,V>(private val global: MutableMap<K,V>, private val local: MutableMap<K,V>) {
    private val safeLocal = local.withDefault { key: K -> getGlobalValue(key) }
    fun getValue(key: K): V = safeLocal.getValue(key)
    fun getGlobalValue(key: K): V = global.getValue(key)
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
        )
        val sm: SmartMap<String, String> = SmartMap(g,l)
        assertThat(sm.getValue("say")).isEqualTo("said")
        assertThat(sm.getValue("go")).isEqualTo("went")
        assertThat(sm.getValue("whazzup")).isEqualTo("I have no idea what whazzup is")
        assertThat(sm.getGlobalValue("say")).isEqualTo("global said")
    }
}