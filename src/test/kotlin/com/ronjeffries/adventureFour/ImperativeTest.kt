package com.ronjeffries.adventureFour

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


val world = world {
    room("name") {}
}

var room = world.unsafeRoomNamed("name")

class ImperativeTest {
    private fun getFactory() = ImperativeFactory(getLexicon())
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
        var imp: Imperative = imperatives.fromOneWord("east")
        assertThat(imp.verb).isEqualTo("go")
        assertThat(imp.noun).isEqualTo("east")
        imp = imperatives.fromOneWord("e")
        assertThat(imp.verb).isEqualTo("go")
        assertThat(imp.noun).isEqualTo("east")
    }

    @Test
    fun `imperative can act`() {
        val imperatives  = getFactory()
        var imp: Imperative = imperatives.fromOneWord("east")
        println("Imp = $imp")
        assertThat(imp.act(testLex())).isEqualTo("went east")
        imp = imperatives.fromOneWord("e")
        assertThat(imp.act(testLex())).isEqualTo("went east")
        imp = Imperative(Phrase("forge", "sword"), world, room)
        assertThat(imp.act(testLex())).isEqualTo("I can't forge a sword")
    }

    @Test
    fun `new phrase finding handles all cases`() {
        val imperatives  = getFactory()
        var imp: Imperative = imperatives.fromTwoWords("take", "cows")
        assertThat(imp.act(testLex())).isEqualTo("no cows for you")
        imp = imperatives.fromTwoWords("hassle","cows")
        assertThat(imp.act(testLex())).isEqualTo("please do not bug the cows")
        imp = imperatives.fromTwoWords("pet","cows")
        assertThat(imp.act(testLex())).isEqualTo("what is it with you and cows?")
        imp = imperatives.fromTwoWords("hassle","bats")
        assertThat(imp.act(testLex())).isEqualTo("please do not bug the bats")
        imp = Imperative(Phrase("forge", "sword"), world, room)
        assertThat(imp.act(testLex())).isEqualTo("I can't forge a sword")
    }

    @Test
    fun `one failing lookup`() {
        val imp = Imperative(Phrase("forge", "sword"), world, room)
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
        assertThat(imp.verb).isEqualTo("go")
        assertThat(imp.noun).isEqualTo("east")
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
    "go" to Imperative(Phrase("go", "irrelevant"), world, room),
    "east" to Imperative(Phrase("go", "east"), world, room),
    "west" to Imperative(Phrase("go", "west"), world, room),
    "north" to Imperative(Phrase("go", "north"), world, room),
    "south" to Imperative(Phrase("go", "south"), world, room),
    "say" to Imperative(Phrase("say", "irrelevant"), world, room),
    "xyzzy" to Imperative(Phrase("say", "xyzzy"), world, room),
    ).withDefault { (Imperative(Phrase(it, "none"), world, room))
}

private val TestActionTable = mutableMapOf(
    Phrase("take", "cows") to {imp: Imperative -> imp.testingSay("no cows for you")},
    Phrase("hassle") to { imp: Imperative -> imp.testingSay("please do not bug the ${imp.noun}")},
    Phrase(noun="cows") to {imp:Imperative -> imp.testingSay("what is it with you and cows?")},
    Phrase("go") to { imp: Imperative -> imp.testingSay("went ${imp.noun}")},
    Phrase("say") to { imp: Imperative -> imp.testingSay("said ${imp.noun}")},
    Phrase("inventory") to { imp: Imperative -> imp.testingSay("You got nothing")},
    Phrase() to { imp: Imperative -> imp.testingSay("I can't ${imp.verb} a ${imp.noun}") }
)