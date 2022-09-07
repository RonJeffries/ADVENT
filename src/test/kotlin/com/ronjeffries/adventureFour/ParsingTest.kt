package com.ronjeffries.adventureFour

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

typealias CMD = String

private fun CMD.parse(): List<String> {
    return this.split(Regex("\\W+"))
}

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
        var result:String = command("e")
        assertThat(result).isEqualTo("move e")
        result = command("sw")
        assertThat(result).isEqualTo("move sw")
        result = command("ns")
        assertThat(result).isEqualTo("I don't understand ns.\n")
        result = command("take axe")
        assertThat(result).isEqualTo("Taken: axe")
        result = command("grab axe")
        assertThat(result).isEqualTo("Taken: axe")
        result = command("fetch axe")
        assertThat(result).isEqualTo("Taken: axe")
        result = command("get axe")
        assertThat(result).isEqualTo("Taken: axe")
        result = command("fake axe")
        assertThat(result).isEqualTo("I don't understand fake axe.\n")
    }
}

private fun command(cmd:CMD): String {
    val words = cmd.parse()
    if (words.isEmpty()) return "no command found"
    return when (words.size) {
        1 -> oneWord(words[0])
        2 -> twoWords(words[0], words[1])
        else -> "Too many words!"
    }
}

fun oneWord(verb: String): String {
    val cardinal = listOf("n","s","e","w")
    val ordinal = listOf("nw", "sw", "ne", "se")
    val action =  when(verb) {
        in cardinal -> ::move
        in ordinal -> ::move
        inVertical(verb) -> ::move
        else -> { return "I don't understand $verb.\n" }
    }
    return action(verb)
}

fun inVertical(verb:String): String {
    val isIn = verb in listOf("up", "dn", "down")
    return if (isIn) verb else ""
}

fun twoWords(verb: String, noun: String): String {
    val isGet:(String)->Boolean = { it in listOf("get","take","fetch", "grab") }
    val action: (String)->String =
        if (isGet(verb)) {
            ::take
        } else
            { return "I don't understand $verb $noun.\n" }
    return action(noun)
}

private fun move(dir: String): String = "move $dir"

fun take(noun:String): String = "Taken: $noun"
