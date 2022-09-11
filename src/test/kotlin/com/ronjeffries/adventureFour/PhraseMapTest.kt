package com.ronjeffries.adventureFour

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

fun makeImperative(): Imperative {
    val world = world {
        room("room") {}
    }
    val room = world.unsafeRoomNamed("room")
    return Imperative("any", "any", world, room)
}

class PhraseMapTest {

    @Test
    fun `phrase map`() {
        val imp = makeImperative()
//        val map = PhraseMap()
        val map = mutableMapOf<Phrase,Action>()
        var p1 = Phrase("take", "cows")
        val p2 = Phrase("take")
        val p3 = Phrase(noun = "bananas")
        val i1: Action = {i:Imperative->"take the cows"}
        val i2: Action = {i-> "take anything"}
        val i3: Action =  {i-> "deal with bananas"}
        assertThat(i1).isEqualTo(i1)
        map.put(p1, i1)
        map.put(p2, i2)
        map.put(p3, i3)
        assertThat(map.get(p1)).isEqualTo(i1)
    }

    @Test
    fun `priority match`() {
        val map: Map<Phrase,String> = makeMap()
        var result:String
        result = find(Phrase("take","cows"), map)
        assertThat(result).isEqualTo("takecows")
    }

    @Test
    fun `take non-cows`() {
        val map = makeMap()
        var result = find(Phrase("take", "bird"), map)
        assertThat(result).isEqualTo("take")
    }

    @Test
    fun `something with cows`() {
        val map = makeMap()
        var result = find(Phrase( noun="cows"), map)
        assertThat(result).isEqualTo("cows")
    }

    @Test
    fun `something different`() {
        val map = makeMap()
        var result = find(Phrase("something", "different"), map)
        assertThat(result).isEqualTo("any")
    }

    private fun makeMap() = mapOf(
        Phrase("take", "cows") to "takecows",
        Phrase("take") to "take",
        Phrase(noun = "cows") to "cows",
        Phrase() to "any"
    )

    fun find(p:Phrase, m:Map<Phrase,String>): String {
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

data class Phrase(val verb: String?=null, val noun: String?=null)

class PhraseMap() {
    val both = mutableMapOf<Phrase,Action>()
    val verbs = mutableMapOf<Phrase,Action>()
    val nouns = mutableMapOf<Phrase,Action>()

    fun put(phrase: Phrase, save: Action) {
        println("putting $save ${save.hashCode()}")
        if (phrase.verb!=null && phrase.noun!=null) {
            both.put(phrase, save)
        } else if (phrase.verb==null && phrase.noun!=null) {
            nouns.put(phrase, save)
        } else if (phrase.verb!=null) {
            verbs.put(phrase,save)
        }
    }

    fun get(phrase: Phrase): Action {
        println("both")
        val fromBoth = both.getOrDefault(phrase, null)
        println("fromBoth $fromBoth ${fromBoth.hashCode()}")
        if (fromBoth != null) return fromBoth
        println("verbs")
        val fromVerbs = verbs.getOrDefault(phrase, null)
        if (fromVerbs != null) return fromVerbs
        println("nouns")
        val fromNouns = nouns.getOrDefault(phrase, null)
        if (fromNouns != null) return fromNouns
        println("nothing")
        return {"not found"}
    }

}