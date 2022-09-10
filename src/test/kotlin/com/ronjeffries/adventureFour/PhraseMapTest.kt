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