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
    fun `single known words with ignored noun`() {
        val command = Command("inventory")
        val result = command
            .validate()
            .execute()
        assertThat(result).isEqualTo("Did inventory with 'ignored'.")

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

