package com.ronjeffries.adventureFour

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class ImperativeTest {
    private fun getFactory() = ImperativeFactory(getLexicon())
    private fun getVerbs() = Verbs(TestVerbTable)
    private fun getSynonyms() = Synonyms(TestSynonymTable)
    private fun getActions() = Actions(TestActionTable)
    private fun getLexicon() = Lexicon(getSynonyms(),getVerbs(),getActions())

    @Test
    fun `create Imperative`() {
        val imp = SimpleImperative("go", "east")
        assertThat(imp.verb).isEqualTo("go")
        assertThat(imp.noun).isEqualTo("east")
    }

    @Test
    fun `look up some imperatives`() {
        val imperatives = getFactory()
        var imp: Imperative = imperatives.fromOneWord("east")
        assertThat(imp).isEqualTo(SimpleImperative("go","east"))
        imp = imperatives.fromOneWord("e")
        assertThat(imp).isEqualTo(SimpleImperative("go","east"))
    }

    @Test
    fun `imperative can act`() {
        val imperatives  = getFactory()
        var imp: Imperative = imperatives.fromOneWord("east")
        assertThat(imp.act(testLex())).isEqualTo("went east")
        imp = imperatives.fromOneWord("e")
        assertThat(imp.act(testLex())).isEqualTo("went east")
        imp = SimpleImperative("forge", "sword")
        assertThat(imp.act(testLex())).isEqualTo("I can't forge a sword")
    }

    private fun testLex(): Lexicon {
        val synonyms = getSynonyms()
        val verbs = getVerbs()
        val actions = getActions()
        return Lexicon(synonyms, verbs, actions)
    }

    @Test
    fun `two word legal command`() {
        val imperatives  = getFactory()
        val imp = imperatives.fromTwoWords("go", "e")
        assertThat(imp.act(testLex())).isEqualTo("went east")
    }

    @Test
    fun `one and two word commands`() {
        val imperatives  = getFactory()
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
        val verbs = getVerbs()
        val imp = verbs.translate("east")
        assertThat(imp).isEqualTo(SimpleImperative("go", "east"))
    }

    @Test
    fun `create a lexicon`() {
        val synonyms = getSynonyms()
        val verbs = getVerbs()
        val actions = getActions()
        val lexicon = Lexicon(synonyms, verbs, actions)
        assertThat(lexicon.synonym("e")).isEqualTo("east")
        val imp: Imperative = lexicon.translate(
            lexicon.synonym("e")
        )
        assertThat(imp.verb).isEqualTo("go")
        assertThat(imp.noun).isEqualTo("east")
        assertThat(imp.act(lexicon)).isEqualTo("went east")
    }

    @Test
    fun `raw string input`() {
        val factory  = getFactory()
        var imp = factory.fromString("east")
        assertThat(imp.verb).isEqualTo("go")
        assertThat(imp.noun).isEqualTo("east")
        imp = factory.fromString("go west")
        assertThat(imp.verb).isEqualTo("go")
        assertThat(imp.noun).isEqualTo("west")
        imp = factory.fromString("Go West")
        assertThat(imp.verb).isEqualTo("go")
        assertThat(imp.noun).isEqualTo("west")
        val tooMany = "too many words"
        imp = factory.fromString(tooMany)
        assertThat(imp.verb).isEqualTo(":tooManyWords")
        assertThat(imp.noun).isEqualTo(tooMany)
        imp = factory.fromString("")
        assertThat(imp.verb).isEqualTo("")
        assertThat(imp.noun).isEqualTo("none")
        val noWords = "234563^%$#@"
        imp = factory.fromString(noWords)
        assertThat(imp.verb).isEqualTo(noWords)
        assertThat(imp.noun).isEqualTo("none")
    }
}

// language control tables. (prototypes)

private val TestSynonymTable = mutableMapOf(
    "e" to "east",
    "n" to "north",
    "w" to "west",
    "s" to "south").withDefault { it }

private val TestVerbTable = mutableMapOf(
    "go" to SimpleImperative("go", "irrelevant"),
    "east" to SimpleImperative("go", "east"),
    "west" to SimpleImperative("go", "west"),
    "north" to SimpleImperative("go", "north"),
    "south" to SimpleImperative("go", "south"),
    "say" to SimpleImperative("say", "irrelevant"),
    "xyzzy" to SimpleImperative("say", "xyzzy"),
    ).withDefault { (SimpleImperative(it, "none"))
}

private val TestActionTable = mutableMapOf(
    "go" to { imp: Imperative -> imp.say("went ${imp.noun}")},
    "say" to { imp: Imperative -> imp.say("said ${imp.noun}")},
    "inventory" to { imp: Imperative -> imp.say("You got nothing")}
).withDefault { { imp: Imperative -> imp.say("I can't ${imp.verb} a ${imp.noun}") }}