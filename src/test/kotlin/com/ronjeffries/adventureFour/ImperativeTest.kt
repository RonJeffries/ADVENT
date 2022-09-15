package com.ronjeffries.adventureFour

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


val world = world {
    room("name") {}
}

var room = world.unsafeRoomNamed("name")

class ImperativeTest {
    private fun getFactory() = PhraseFactory(getLexicon())
    private fun getVerbs() = Verbs(TestVerbTable)
    private fun getSynonyms() = Synonyms(TestSynonymTable)
    private fun getActions() = Actions(TestActionTable)
    private fun getLexicon() = Lexicon(getSynonyms(),getVerbs(),getActions())

    @Test
    fun `create Imperative`() {
        val imp = Imperative(Phrase("go", "east"), world, room)
        assertThat(imp.verb).isEqualTo("go")
        assertThat(imp.noun).isEqualTo("east")
    }

    @Test
    fun `look up some imperatives`() {
        val imperatives = getFactory()
        var phrase: Phrase = imperatives.fromOneWord("east")
        assertThat(phrase.verb).isEqualTo("go")
        assertThat(phrase.noun).isEqualTo("east")
        phrase = imperatives.fromOneWord("e")
        assertThat(phrase.verb).isEqualTo("go")
        assertThat(phrase.noun).isEqualTo("east")
    }

    @Test
    fun `imperative can act`() {
        val factory = getFactory()
        var imp = factory.fromOneWord("east").asImperative()
        assertThat(imp.actForTesting(testLex())).isEqualTo("went east")
        imp = factory.fromOneWord("e").asImperative()
        assertThat(imp.actForTesting(testLex())).isEqualTo("went east")
        imp = Imperative(Phrase("forge", "sword"), world, room)
        assertThat(imp.actForTesting(testLex())).isEqualTo("I can't forge a sword")
    }

    @Test
    fun `new phrase finding handles all cases`() {
        val imperatives  = getFactory()
        var imp: Imperative = imperatives.fromTwoWords("take", "cows").asImperative()
        assertThat(imp.actForTesting(testLex())).isEqualTo("no cows for you")
        imp = imperatives.fromTwoWords("hassle","cows").asImperative()
        assertThat(imp.actForTesting(testLex())).isEqualTo("please do not bug the cows")
        imp = imperatives.fromTwoWords("pet","cows").asImperative()
        assertThat(imp.actForTesting(testLex())).isEqualTo("what is it with you and cows?")
        imp = imperatives.fromTwoWords("hassle","bats").asImperative()
        assertThat(imp.actForTesting(testLex())).isEqualTo("please do not bug the bats")
        imp = Imperative(Phrase("forge", "sword"), world, room)
        assertThat(imp.actForTesting(testLex())).isEqualTo("I can't forge a sword")
    }

    @Test
    fun `one failing lookup`() {
        val imp = Imperative(Phrase("forge", "sword"), world, room)
        assertThat(imp.actForTesting(testLex())).isEqualTo("I can't forge a sword")
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
        val imp = imperatives.fromTwoWords("go", "e").asImperative()
        assertThat(imp.actForTesting(testLex())).isEqualTo("went east")
    }

    @Test
    fun `one and two word commands`() {
        val factory  = getFactory()
        var imp = factory.fromTwoWords("go", "w").asImperative()
        assertThat(imp.actForTesting(testLex())).isEqualTo("went west")
        imp = factory.fromOneWord("w").asImperative()
        assertThat(imp.actForTesting(testLex())).isEqualTo("went west")
        imp = factory.fromOneWord("xyzzy").asImperative()
        assertThat(imp.actForTesting(testLex())).isEqualTo("said xyzzy")
        imp = factory.fromTwoWords("say", "xyzzy").asImperative()
        assertThat(imp.actForTesting(testLex())).isEqualTo("said xyzzy")
        imp = factory.fromTwoWords("say", "unknownWord").asImperative()
        assertThat(imp.actForTesting(testLex())).isEqualTo("said unknownWord")
        imp = factory.fromTwoWords("unknownVerb", "unknownNoun").asImperative()
        assertThat(imp.actForTesting(testLex())).isEqualTo("I can't unknownVerb a unknownNoun")
        imp = factory.fromOneWord("unknownWord").asImperative()
        assertThat(imp.actForTesting(testLex())).isEqualTo("I can't unknownWord a none")
        imp = factory.fromOneWord("inventory").asImperative()
        assertThat(imp.actForTesting(testLex())).isEqualTo("You got nothing")
    }

    @Test
    fun verbTranslator() {
        val verbs = getVerbs()
        val phrase = verbs.translate("east").phrase
        assertThat(phrase.verb).isEqualTo("go")
        assertThat(phrase.noun).isEqualTo("east")
    }

    @Test
    fun `create a lexicon`() {
        val synonyms = getSynonyms()
        val verbs = getVerbs()
        val actions = getActions()
        val lexicon = Lexicon(synonyms, verbs, actions)
        assertThat(lexicon.synonym("e")).isEqualTo("east")
        val phrase: Phrase = lexicon.translate(
            lexicon.synonym("e")
        ).phrase
        assertThat(phrase.phrase).isEqualTo(phrase)
        val imp = Imperative(phrase, world, room)
        assertThat(imp.verb).isEqualTo("go")
        assertThat(imp.noun).isEqualTo("east")
        assertThat(imp.actForTesting(lexicon)).isEqualTo("went east")
    }

    @Test
    fun `raw string input`() {
        val factory  = getFactory()
        var phrase = factory.fromString("east")
        assertThat(phrase.verb).isEqualTo("go")
        assertThat(phrase.noun).isEqualTo("east")
        phrase = factory.fromString("go west")
        assertThat(phrase.verb).isEqualTo("go")
        assertThat(phrase.noun).isEqualTo("west")
        phrase = factory.fromString("Go West")
        assertThat(phrase.verb).isEqualTo("go")
        assertThat(phrase.noun).isEqualTo("west")
        val tooMany = "too many words"
        phrase = factory.fromString(tooMany)
        assertThat(phrase.verb).isEqualTo(":tooManyWords")
        assertThat(phrase.noun).isEqualTo(tooMany)
        phrase = factory.fromString("")
        assertThat(phrase.verb).isEqualTo("")
        assertThat(phrase.noun).isEqualTo("none")
        val noWords = "234563^%$#@"
        phrase = factory.fromString(noWords)
        assertThat(phrase.verb).isEqualTo(noWords)
        assertThat(phrase.noun).isEqualTo("none")
    }
}

// language control tables. (prototypes)

private val TestSynonymTable = mutableMapOf(
    "e" to "east",
    "n" to "north",
    "w" to "west",
    "s" to "south").withDefault { it }

private val TestVerbTable = mutableMapOf(
    "go" to Phrase("go", "irrelevant"),
    "east" to Phrase("go", "east"),
    "west" to Phrase("go", "west"),
    "north" to Phrase("go", "north"),
    "south" to Phrase("go", "south"),
    "say" to Phrase("say", "irrelevant"),
    "xyzzy" to Phrase("say", "xyzzy"),
    ).withDefault { Phrase(it, "none") }

private val TestActionTable = mutableMapOf(
    Phrase("take", "cows") to {imp: Imperative -> imp.testingSay("no cows for you")},
    Phrase("hassle") to { imp: Imperative -> imp.testingSay("please do not bug the ${imp.noun}")},
    Phrase(noun="cows") to {imp:Imperative -> imp.testingSay("what is it with you and cows?")},
    Phrase("go") to { imp: Imperative -> imp.testingSay("went ${imp.noun}")},
    Phrase("say") to { imp: Imperative -> imp.testingSay("said ${imp.noun}")},
    Phrase("inventory") to { imp: Imperative -> imp.testingSay("You got nothing")},
    Phrase() to { imp: Imperative -> imp.testingSay("I can't ${imp.verb} a ${imp.noun}") }
)