package com.ronjeffries.adventureFour

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class StatusTest {
    @Test
    fun `status isTrue`() {
        val status = GameStatus()
        assertThat(status.isTrue).isEqualTo(false)
        status.set(true)
        assertThat(status.isTrue).isEqualTo(true)
        assertThat(status.isFalse).isEqualTo(false)
        status.increment()
        assertThat(status.value).isEqualTo(2)
        status.increment()
        assertThat(status.value).isEqualTo(3)
        assertThat(status.isTrue).describedAs("three is true").isEqualTo(true)
        status.not()
        assertThat(status.isTrue).isEqualTo(false)
        assertThat(status.value).isEqualTo(0)
    }

    @Test
    fun `Status map`() {
        val map = GameStatusMap().apply {
            assertThat(get("unlocked").isFalse).isEqualTo(true)
            get("unlocked").not()
            assertThat(get("unlocked").isTrue).isEqualTo(true)
        }
        assertThat(map.truth("unlocked")).isEqualTo(true)
        map.not("unlocked")
        assertThat(map.truth("unlocked")).isEqualTo(false)
        map.get("counter").set(5)
        assertThat(map.get("counter").value).isEqualTo(5)
    }
}

