package com.ronjeffries.adventureFour

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.reflect.KFunction


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
        command.expandIfNeeded()
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
            .expandIfNeeded()
            .makeVerbNoun()
            .findOperation()
    }

    fun makeWords(): Command {
        words += input.split(" ")
        return this
    }

    fun makeVerbNoun(): Command {
        verb = words[0]
        noun = words[1]
        return this
    }

    fun expandIfNeeded(): Command {
        if (words.size == 2) return this
        val directions = listOf(
            "n","e","s","w","north","east","south","west",
            "nw","northwest", "sw","southwest", "ne", "northeast", "se", "southeast",
            "up","dn","down")

        val magicWords = listOf("xyzzy", "plugh")
        val word = words[0]
        if (word in directions) {
            words.add(0,"go")
        } else if (word in magicWords) {
            words.add(0,"say")
        } else {
            words[0] = "verbError"
            words.add(word)
        }
        return this
    }

    fun findOperation(): Command {
        val test = ::take
        if (verb=="take")
            operation = ::take
        else if (verb == "go")
            operation = ::go
        else if (verb == "say")
            operation = ::say
        else if (verb == "verbError")
            operation = ::verbError
        return this
    }

    fun verbError(noun: String) {
        result = "I don't understand $noun."
    }

    fun go(noun: String) {
        result = "went $noun."
    }

    fun say(noun:String) {
        result = "said $noun."
    }

    fun take(noun:String) {
        result = "$noun taken."
    }

    fun commandError(noun: String) {
        result = "command error"
    }

    fun execute(): String {
        operation(noun)
        return result
    }
}