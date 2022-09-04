package com.ronjeffries.adventureFour

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class ImperativeSpike {
    @Test
    fun `create Imperative`() {
        val imp = Imperative("go", "east")
        assertThat(imp.verb).isEqualTo("go")
        assertThat(imp.noun).isEqualTo("east")
    }

    @Test
    fun `look up some imperatives`() {
        val imperatives = ImperativeFactory(Verbs(TestVerbTable))
        var imp: Imperative = imperatives.fromOneWord("east")
        assertThat(imp).isEqualTo(Imperative("go","east"))
        imp = imperatives.fromOneWord("e")
        assertThat(imp).isEqualTo(Imperative("go","east"))
    }

    @Test
    fun `imperative can act`() {
        val imperatives = ImperativeFactory(Verbs(TestVerbTable))
        var imp: Imperative = imperatives.fromOneWord("east")
        assertThat(imp.act(testLex())).isEqualTo("went east")
        imp = imperatives.fromOneWord("e")
        assertThat(imp.act(testLex())).isEqualTo("went east")
        imp = Imperative("forge", "sword")
        assertThat(imp.act(testLex())).isEqualTo("I can't forge a sword")
    }

    fun testLex(): Lexicon {
        val synonyms = Synonyms(TestSynonymTable)
        val verbs = Verbs(TestVerbTable)
        val actions = Actions(TestActionTable)
        return Lexicon(synonyms, verbs, actions)
    }

    @Test
    fun `two word legal command`() {
        val imperatives = ImperativeFactory(TestVerbTable)
        val imp = imperatives.fromTwoWords("go", "e")
        assertThat(imp.act(testLex())).isEqualTo("went east")
    }

    @Test
    fun `one and two word commands`() {
        val imperatives = ImperativeFactory(TestVerbTable)
        var imp = imperatives.fromTwoWords("go", "w")
        assertThat(imp.act(testLex())).isEqualTo("went west")
        imp = imperatives.fromOneWord("w")
        assertThat(imp.act(testLex())).isEqualTo("went west")
        imp = imperatives.fromOneWord("xyzzy")
        assertThat(imp.act(testLex())).isEqualTo("said xyzzy")
        imp = imperatives.fromTwoWords("say", "xyzzy")
        assertThat(imp.act(testLex())).isEqualTo("said xyzzy")
        imp = imperatives.fromTwoWords("say", "unknownWord")
        assertThat(imp.act(testLex())).isEqualTo("said unknownWord")
        imp = imperatives.fromTwoWords("unknownVerb", "unknownNoun")
        assertThat(imp.act(testLex())).isEqualTo("I can't unknownVerb a unknownNoun")
        imp = imperatives.fromOneWord("unknownWord")
        assertThat(imp.act(testLex())).isEqualTo("I can't unknownWord a none")
        imp = imperatives.fromOneWord("inventory")
        assertThat(imp.act(testLex())).isEqualTo("You got nothing")
    }

    @Test
    fun verbTranslator() {
        val vt = Verbs(TestVerbTable)
        val imp = vt.translate("east")
        assertThat(imp).isEqualTo(Imperative("go", "east"))
    }

    @Test
    fun `create a lexicon`() {
        val synonyms = Synonyms(TestSynonymTable)
        val verbs = Verbs(TestVerbTable)
        val actions = Actions(TestActionTable)
        val lexicon = Lexicon(synonyms, verbs, actions)
        assertThat(lexicon.synonym("e")).isEqualTo("east")
        val imp: Imperative = lexicon.translate(
            lexicon.synonym("e")
        )
        assertThat(imp.verb).isEqualTo("go")
        assertThat(imp.noun).isEqualTo("east")
        assertThat(imp.act(lexicon)).isEqualTo("went east")
    }
}

class Lexicon(val synonyms: Synonyms, val verbs: Verbs, val actions: Actions) {
    fun synonym(word:String):String = synonyms.synonym(word)
    fun translate(word: String): Imperative = verbs.translate((word))
    fun act(imperative: Imperative) = actions.act(imperative)
}


data class Imperative(val verb: String, val noun: String) {
    fun setNoun(noun: String): Imperative = Imperative(verb,noun)

    fun act(lexicon: Lexicon):String {
        lexicon.act(this)
        return said
    }
    var said: String = ""

    fun say(s:String) {
        said = s
    }
}

class ImperativeFactory(private val verbs:Verbs, private val synonyms:Synonyms = Synonyms(TestSynonymTable)) {
    constructor(map: Map<String, Imperative>) : this(Verbs(map))

    fun fromOneWord(verb:String): Imperative = imperative(verb)
    fun fromTwoWords(verb:String, noun:String) = imperative(verb).setNoun(synonym(noun))
    private fun imperative(verb: String) = verbs.translate(synonym(verb))
    private fun synonym(verb: String) = synonyms.synonym(verb)
}

class Verbs(private val map:Map<String,Imperative>) {
    fun translate(verb:String): Imperative = map.getValue(verb)
}

class Synonyms(private val map: Map<String,String>) {
    fun synonym(word:String) = map.getValue(word)
}

typealias Action = (Imperative) -> Unit

class Actions(private val verbMap:Map<String, Action> = TestActionTable) {
    fun act(imperative:Imperative) {
         verbMap.getValue((imperative.verb))(imperative)
    }
}

// language control tables. (prototypes)

val TestActionTable = mapOf(
    "go" to { imp:Imperative -> imp.say("went ${imp.noun}")},
    "say" to { imp:Imperative -> imp.say("said ${imp.noun}")},
    "inventory" to { imp:Imperative -> imp.say("You got nothing")}
).withDefault {it ->{imp:Imperative -> imp.say("I can't ${imp.verb} a ${imp.noun}") }}

val TestSynonymTable = mapOf(
    "e" to "east",
    "n" to "north",
    "w" to "west",
    "s" to "south").withDefault { it }

val TestVerbTable = mapOf(
    "go" to Imperative("go", "irrelevant"),
    "east" to Imperative("go","east"),
    "west" to Imperative("go","west"),
    "north" to Imperative("go","north"),
    "south" to Imperative("go","south"),
    "say" to Imperative("say", "irrelevant"),
    "xyzzy" to Imperative("say", "xyzzy"),
    ).withDefault { (Imperative(it, "none"))
}
