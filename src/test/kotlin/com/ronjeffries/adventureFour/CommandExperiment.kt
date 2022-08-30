package com.ronjeffries.adventureFour

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class CommandExperiment {

    @Test
    fun sequence() {
        val command = Command("take axe")
        val result = command
            .validate()
            .execute()
        assertThat(result).isEqualTo("axe taken.")
    }

    @Test
    fun `go command`() {
        val command = Command("go east")
        val result = command
            .validate()
            .execute()
        assertThat(result).isEqualTo("went east.")
    }

    @Test
    fun `expand magic`() {
        val command = Command("xyzzy")
        command.makeWords()
        assertThat(command.words.size).isEqualTo(1)
        command.magicWords()
        assertThat(command.words.size).isEqualTo(2)
        assertThat(command.words[0]).isEqualTo("say")
        assertThat(command.words[1]).isEqualTo("xyzzy")
//        command.makeVerbNoun()
        assertThat(command.verb).isEqualTo("say")
        assertThat(command.noun).isEqualTo("xyzzy")
    }

    @Test
    fun `single word go commands`() {
        val command = Command("east")
        val result = command
            .validate()
            .execute()
        assertThat(result).isEqualTo("went east.")
    }

    @Test
    fun `single magic word commands`() {
        val command = Command("xyzzy")
        val result = command
            .validate()
            .execute()
        assertThat(result).isEqualTo("said xyzzy.")
    }

    @Test
    fun `single unknown word commands`() {
        val command = Command("fragglerats")
        val result = command
            .validate()
            .execute()
        assertThat(result).isEqualTo("I don't understand fragglerats.")
    }

    @Test
    fun `evoke command error`() {
        val command = Command("vorpal blade")
        val result = command
            .validate()
            .execute()
        assertThat(result).isEqualTo("command error 'vorpal blade'.")
    }

    @Test
    fun `too many words`() {
        val command = Command("too many words")
        val result = command
            .validate()
            .execute()
        assertThat(result).isEqualTo("I understand only one- and two-word commands, not 'too many words'.")
    }
}

private class Command(val input: String) {
    val words = mutableListOf<String>()
    var operation = this::commandError
    var result: String = ""
    val verb get() = words[0]
    val noun get() = words[1]

    fun validate(): Command{
        return this
            .makeWords()
            .oneOrTwoWords()
            .goWords()
            .magicWords()
            .errorIfOnlyOneWord()
            .findOperation()
    }

    fun makeWords(): Command {
        words += input.split(" ")
        return this
    }

    fun oneOrTwoWords(): Command {
        if (words.size < 1 || words.size > 2 ){
            words.clear()
            words.add("countError")
            words.add(input)
        }
        return this
    }

    fun errorIfOnlyOneWord(): Command {
        if (words.size == 2) return this
        words.add(0,"verbError")
        return this
    }

    fun findOperation(): Command {
        operation = when (verb) {
            "take" -> ::take
            "go" -> ::go
            "say" -> ::say
            "verbError" -> ::verbError
            "countError" -> ::countError
            else -> ::commandError
        }
        return this
    }

    fun goWords(): Command {
        val directions = listOf(
            "n","e","s","w","north","east","south","west",
            "nw","northwest", "sw","southwest", "ne", "northeast", "se", "southeast",
            "up","dn","down")
        return substituteSingle("go", directions)
    }

    fun magicWords(): Command {
        val magicWords = listOf("xyzzy", "plugh")
        return substituteSingle("say", magicWords)
    }

    fun substituteSingle(sub: String, singles: List<String>): Command {
        if (words.size == 2) return this
        if (words[0] in singles) words.add(0, sub)
        return this
    }

    // execution

    fun execute(): String {
        result = operation(noun)
        return result
    }

    fun commandError(noun: String) : String = "command error '$input'."
    fun go(noun: String): String = "went $noun."
    fun say(noun:String): String = "said $noun."
    fun take(noun:String): String = "$noun taken."
    fun verbError(noun: String): String = "I don't understand $noun."
    fun countError(noun: String): String = "I understand only one- and two-word commands, not '$noun'."
}