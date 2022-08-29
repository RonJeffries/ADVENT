package com.ronjeffries.adventureFour

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class CommandExperiment {

    @Test
    fun `sequence`() {
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
        command.makeVerbNoun()
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
}

private class Command(val input: String) {
    var verb = ""
    var noun = ""
    val words = mutableListOf<String>()
    var operation = this::commandError
    var result: String = ""

    fun validate(): Command{
        return this
            .makeWords()
            .goWords()
            .magicWords()
            .errorIfOnlyOneWord()
            .makeVerbNoun()
            .findOperation()
    }

    fun makeWords(): Command {
        words += input.split(" ")
        return this
    }

    fun errorIfOnlyOneWord(): Command {
        if (words.size == 2) return this
        words.add(0,"verbError")
        return this
    }

    fun goWords(): Command {
        if (words.size == 2) return this
        val directions = listOf(
            "n","e","s","w","north","east","south","west",
            "nw","northwest", "sw","southwest", "ne", "northeast", "se", "southeast",
            "up","dn","down")
        if (words[0] in directions) {
            words.add(0, "go")
        }
        return this
    }

    fun magicWords(): Command {
        if (words.size == 2) return this
        val magicWords = listOf("xyzzy", "plugh")
        if (words[0] in magicWords) {
            words.add(0,"say")
        }
        return this
    }

    fun makeVerbNoun(): Command {
        verb = words[0]
        noun = words[1]
        return this
    }

    fun findOperation(): Command {
        val test = ::take
        operation = when (verb) {
            "take" -> ::take
            "go" -> ::go
            "say" -> ::say
            "verbError" -> ::verbError
            else -> ::commandError
        }
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
}