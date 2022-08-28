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
        result = command("fake axe")
        assertThat(result).isEqualTo("I don't understand fake axe.\n")
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

fun oneWordX(verb: String): String {
    val action =  when(verb) {
        "n","s","e","w" -> ::move
        "nw","sw","ne","se" -> ::move
        "up","dn","down" -> ::move
        else -> { return "I don't understand $verb.\n" }
    }
    return action(verb)
}

fun oneWord(verb: String): String {
    val cardinal = listOf<String>("n","s","e","w")
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
    if (isIn) return verb else return ""
}

fun twoWords(verb: String, noun: String): String {
    val isGet:(String)->Boolean = { it == "get"}
    val isTake:(String)->Boolean = { it == "take"}
    val action: (String)->String =
        if (isGet(verb)) {
            ::take
        } else if (isTake(verb))
            ::take
        else
            { return "I don't understand $verb $noun.\n" }
    return action(noun)
}

private fun move(dir: String): String = "move $dir"

fun take(noun:String): String = "Taken: $noun"

private fun String.parse(): List<String> {
    return this.split(Regex("\\W+"))
}