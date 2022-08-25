package com.ronjeffries.adventureFour

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class StatusTest {
    @Test
    fun `status isTrue`() {
        val status = BooleanStatus()
        assertThat(status.isTrue()).isEqualTo(false)
        status.set(true)
        assertThat(status.isTrue()).isEqualTo(true)

    }

    @Test
    fun `Status map`() {
        StatusMap().apply {
            assertThat(item("unlocked").isFalse()).isEqualTo(true)
            item("unlocked").not()
            assertThat(item("unlocked").isTrue()).isEqualTo(true)
        }
    }
}

class StatusMap {
    val map = mutableMapOf<String, BooleanStatus>()

    fun item(name:String): BooleanStatus {
        return map.getOrPut(name) { BooleanStatus() }
    }
}

class BooleanStatus(var value: Boolean = false) {

    fun set(truth: Boolean) {
        value = truth
    }

    fun isTrue(): Boolean {
        return value
    }

    fun isFalse(): Boolean {
        return !value
    }

    fun not(): Unit {
        value = !value
    }
}