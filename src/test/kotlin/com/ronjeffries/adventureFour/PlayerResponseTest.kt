package com.ronjeffries.adventureFour

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PlayerResponseTest {
    @Test
    fun `response has say method`() {
        val r = GameResponse(Player(World(),R.Z_FIRST))
        r.say("One thing")
        r.say("Another thing")
        assertThat(r.sayings).isEqualTo("One thing\nAnother thing\n")
    }
}