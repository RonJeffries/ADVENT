package com.ronjeffries.adventureFour

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class StatusTest {
    @Test
    fun `status isTrue`() {
        val status = BooleanStatus()
        assertThat(status.isTrue).isEqualTo(false)
        status.set(true)
        assertThat(status.isTrue).isEqualTo(true)
        assertThat(status.isFalse).isEqualTo(false)

    }

    @Test
    fun `Status map`() {
        BooleanStatusMap().apply {
            assertThat(get("unlocked").isFalse).isEqualTo(true)
            get("unlocked").not()
            assertThat(get("unlocked").isTrue).isEqualTo(true)
        }
    }
}

