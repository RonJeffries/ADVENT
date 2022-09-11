package com.ronjeffries.adventureFour

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

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
    fun `match search in Actions`() {

    }

}