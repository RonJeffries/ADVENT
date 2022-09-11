package com.ronjeffries.adventureFour

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PhraseMapTest {

    @Test
    fun `phrase map`() {
//        val map = PhraseMap()
        val map = mutableMapOf<Phrase,Action>()
        val p1 = Phrase("take", "cows")
        val p2 = Phrase("take")
        val p3 = Phrase(noun = "bananas")
        val i1: Action = { _:Imperative->"take the cows"}
        val i2: Action = { _ -> "take anything"}
        val i3: Action =  { _ -> "deal with bananas"}
        assertThat(i1).isEqualTo(i1)
        map[p1] = i1
        map[p2] = i2
        map[p3] = i3
        assertThat(map[p1]).isEqualTo(i1)
    }

    @Test
    fun `priority match`() {
        val map: Map<Phrase,String> = makeMap()
        val result:String = find(Phrase("take","cows"), map)
        assertThat(result).isEqualTo("takecows")
    }

    @Test
    fun `take non-cows`() {
        val map = makeMap()
        val result = find(Phrase("take", "bird"), map)
        assertThat(result).isEqualTo("take")
    }

    @Test
    fun `something with cows`() {
        val map = makeMap()
        val result = find(Phrase( noun="cows"), map)
        assertThat(result).isEqualTo("cows")
    }

    @Test
    fun `something different`() {
        val map = makeMap()
        val result = find(Phrase("something", "different"), map)
        assertThat(result).isEqualTo("any")
    }

    private fun makeMap() = mapOf(
        Phrase("take", "cows") to "takecows",
        Phrase("take") to "take",
        Phrase(noun = "cows") to "cows",
        Phrase() to "any"
    )

    private fun find(p:Phrase, m: Map<Phrase, String>): String {
        return m.getOrElse(p) {
            m.getOrElse(p.asVerb()) {
                m.getOrElse(p.asNoun()) {
                    m.getOrElse(p.asEmpty()) {"nothing"}
                }
            }
        }
    }

    private fun oldFind(p:Phrase, m:Map<Phrase,String>): String {
        val p2 = Phrase(p.verb)
        val p3 = Phrase(noun=p.noun)
        val p4 = Phrase()
        var results = listOf(p,p2,p3,p4).map {m.getOrDefault(it,"nope")}
        print("$p->")
        print("$results->")
        results = results.filter { it != "nope" }
        println("$results")
        return results.first()
    }
}


