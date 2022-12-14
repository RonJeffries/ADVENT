package com.ronjeffries.adventureFour

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach


val world = world {
    room(R.Z_FIRST) {}
}

var room = R.Z_FIRST.room

class ImperativeTest {
    private fun getFactory() = PhraseFactory(getLexicon())
    private fun getVerbs() = Verbs(TestVerbTable)
    private fun getSynonyms() = Synonyms(TestSynonymTable)
    private fun getActions() = makeTestActions()
    private fun getLexicon() = Lexicon(getSynonyms(), getVerbs())

    @BeforeEach
    fun `before each`() {
        testSetUpLexAndActions()
    }

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
        assertThat(imp.actForTesting(testActions())).isEqualTo("went east")
        imp = factory.fromOneWord("e").asImperative()
        assertThat(imp.actForTesting(testActions())).isEqualTo("went east")
        imp = Imperative(Phrase("forge", "sword"), world, room)
        assertThat(imp.actForTesting(testActions())).isEqualTo("I can't forge a sword")
    }

    @Test
    fun `new phrase finding handles all cases`() {
        val imperatives  = getFactory()
        var imp: Imperative = imperatives.fromTwoWords("take", "cows").asImperative()
        assertThat(imp.actForTesting(testActions())).isEqualTo("no cows for you")
        imp = imperatives.fromTwoWords("hassle","cows").asImperative()
        assertThat(imp.actForTesting(testActions())).isEqualTo("please do not bug the cows")
        imp = imperatives.fromTwoWords("pet","cows").asImperative()
        assertThat(imp.actForTesting(testActions())).isEqualTo("what is it with you and cows?")
        imp = imperatives.fromTwoWords("hassle","bats").asImperative()
        assertThat(imp.actForTesting(testActions())).isEqualTo("please do not bug the bats")
        imp = Imperative(Phrase("forge", "sword"), world, room)
        assertThat(imp.actForTesting(testActions())).isEqualTo("I can't forge a sword")
    }

    @Test
    fun `one failing lookup`() {
        val imp = Imperative(Phrase("forge", "sword"), world, room)
        assertThat(imp.actForTesting(testActions())).isEqualTo("I can't forge a sword")
    }

    private fun testSetUpLexAndActions(): Lexicon {
        val synonyms = getSynonyms()
        val verbs = getVerbs()
        getActions()
        return Lexicon(synonyms, verbs)
    }

    private fun testActions(): IActions {
        return world.actions
    }

    @Test
    fun `two word legal command`() {
        val imperatives  = getFactory()
        val imp = imperatives.fromTwoWords("go", "e").asImperative()
        assertThat(imp.actForTesting(testActions())).isEqualTo("went east")
    }

    @Test
    fun `one and two word commands`() {
        val factory  = getFactory()
        var imp = factory.fromTwoWords("go", "w").asImperative()
        assertThat(imp.actForTesting(testActions())).isEqualTo("went west")
        imp = factory.fromOneWord("w").asImperative()
        assertThat(imp.actForTesting(testActions())).isEqualTo("went west")
        imp = factory.fromOneWord("xyzzy").asImperative()
        assertThat(imp.actForTesting(testActions())).isEqualTo("said xyzzy")
        imp = factory.fromTwoWords("say", "xyzzy").asImperative()
        assertThat(imp.actForTesting(testActions())).isEqualTo("said xyzzy")
        imp = factory.fromTwoWords("say", "unknownWord").asImperative()
        assertThat(imp.actForTesting(testActions())).isEqualTo("said unknownWord")
        imp = factory.fromTwoWords("unknownVerb", "unknownNoun").asImperative()
        assertThat(imp.actForTesting(testActions())).isEqualTo("I can't unknownVerb a unknownNoun")
        imp = factory.fromOneWord("unknownWord").asImperative()
        assertThat(imp.actForTesting(testActions())).isEqualTo("I can't unknownWord a none")
        imp = factory.fromOneWord("inventory").asImperative()
        assertThat(imp.actForTesting(testActions())).isEqualTo("You got nothing")
    }

    @Test
    fun verbTranslator() {
        val verbs = getVerbs()
        val phrase = verbs.translate("east")
        assertThat(phrase.verb).isEqualTo("go")
        assertThat(phrase.noun).isEqualTo("east")
    }

    @Test
    fun `create a lexicon`() {
        val synonyms = getSynonyms()
        val verbs = getVerbs()
        val lexicon = Lexicon(synonyms, verbs)
        assertThat(lexicon.synonym("e")).isEqualTo("east")
        val phrase: Phrase = lexicon.translate(
            lexicon.synonym("e")
        )
        val imp = Imperative(phrase, world, room)
        assertThat(imp.verb).isEqualTo("go")
        assertThat(imp.noun).isEqualTo("east")
        assertThat(imp.actForTesting(world.actions)).isEqualTo("went east")
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

private fun makeTestActions() {
    world.actions.clear()
    with(world.actions) {
        add(Phrase("take", "cows")) { it.testingSay("no cows for you") }
        add(Phrase("hassle")) { it.testingSay("please do not bug the ${it.noun}") }
        add(Phrase(noun = "cows")) { it.testingSay("what is it with you and cows?") }
        add(Phrase("go")) { it.testingSay("went ${it.noun}") }
        add(Phrase("say")) { it.testingSay("said ${it.noun}") }
        add(Phrase("inventory")) { it.testingSay("You got nothing") }
        add(Phrase()) { it.testingSay("I can't ${it.verb} a ${it.noun}") }
    }
}
