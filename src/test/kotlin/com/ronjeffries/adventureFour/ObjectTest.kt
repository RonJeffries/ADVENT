package com.ronjeffries.adventureFour

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class ObjectTest {
    @Test
    fun `object`() {
        val flags = object {
            var b = false
            var i = 0
        }
        assertThat(flags.b).isEqualTo(false)
        flags.b = true
        assertThat(flags.b).isEqualTo(true)
        flags.i++
        assertThat(flags.i).isEqualTo(1)
    }
}