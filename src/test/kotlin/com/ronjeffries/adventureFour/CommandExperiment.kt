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

    fun `single word commands`() {
        val command = Command("east")
        val result = command
            .validate()
            .execute()
        assertThat(result).isEqualTo("went east.")
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
        val word = words[0]
        if (word in directions) {
            words[1] = words[0]
            words[0] = "go"
        }
        return this
    }

    fun findOperation(): Command {
        val test = ::take
        if (verb=="take")
            operation = ::take
        else if (verb == "go")
            operation = ::go
        return this
    }

    fun go(noun: String) {
        result = "went $noun."
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