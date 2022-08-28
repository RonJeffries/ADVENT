package com.ronjeffries.adventureFour

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class ParsingTest {
    @Test
    fun `pattern splitter`() {
        val regex = Regex("\\W+")
        val words = "take axe".split(regex)
        assertThat(words.size).isEqualTo(2)
        assertThat(words[0]).isEqualTo("take")
        assertThat(words[1]).isEqualTo("axe")
    }

    @Test
    fun `identify a few commands`() {
        val words = "take axe".parse()
        assertThat(words.size).isEqualTo(2)
    }

    @Test
    fun `some commands`() {
        var result:String
        result = command("e")
        assertThat(result).isEqualTo("move e")
        result = command("sw")
        assertThat(result).isEqualTo("move sw")
        result = command("ns")
        assertThat(result).isEqualTo("I don't understand ns.\n")
        result = command("take axe")
        assertThat(result).isEqualTo("Taken: axe")
    }
}

private fun command(cmd:String): String {
    val words = cmd.parse()
    if (words.size == 0) return "no command found"
    val verb = words[0]
    var noun = ""
    return if (words.size == 1)
        oneWord(words[0])
    else if (words.size == 2)
        twoWords(words[0], words[1])
    else
        "Too many words!"
}

fun oneWord(verb: String): String {
    val action =  when(verb) {
        "n","s","e","w" -> ::move
        "nw","sw","ne","se" -> ::move
        "up","dn","down" -> ::move
        else -> { return "I don't understand $verb.\n" }
    }
    return action(verb)
}

fun twoWords(verb: String, noun: String): String {
    return when(verb) {
        "take","get" -> take(noun)
        else -> "I don't understand $verb $noun.\n"
    }
}

private fun move(dir: String): String = "move $dir"

fun take(noun:String): String = "Taken: $noun"

private fun String.parse(): List<String> {
    return this.split(Regex("\\W+"))
}