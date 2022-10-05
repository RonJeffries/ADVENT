package com.ronjeffries.adventureFour

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FactsTest {
    @Test
    fun `status isTrue`() {
        val fact = Fact()
        assertThat(fact.isTrue).isEqualTo(false)
        fact.set(true)
        assertThat(fact.isTrue).isEqualTo(true)
        assertThat(fact.isFalse).isEqualTo(false)
        fact.increment()
        assertThat(fact.value).isEqualTo(2)
        fact.increment()
        assertThat(fact.value).isEqualTo(3)
        assertThat(fact.isTrue).describedAs("three is true").isEqualTo(true)
        fact.not()
        assertThat(fact.isTrue).isEqualTo(false)
        assertThat(fact.value).isEqualTo(0)
    }

    @Test
    fun `Status map`() {
        val facts = Facts()
        assertThat(facts.truth("unlocked")).isEqualTo(false)
        assertThat(facts.isTrue("unlocked")).isEqualTo(false)
        assertThat(facts.isFalse("unlocked")).isEqualTo(true)
        facts.not("unlocked")
        assertThat(facts.truth("unlocked")).isEqualTo(true)
        assertThat(facts.isTrue("unlocked")).isEqualTo(true)
        assertThat(facts.isFalse("unlocked")).isEqualTo(false)
        facts.set("counter", 5)
        assertThat(facts.value("counter")).isEqualTo(5)
        facts.increment("counter")
        assertThat(facts.value("counter")).isEqualTo(6)
        facts.decrement(("counter"))
        assertThat(facts.value("counter")).isEqualTo(5)
    }

    @Test
    fun `subscripting format`() {
        val facts = Facts()
        assertThat(facts["unlocked"].isTrue).isEqualTo(false)
    }
}

