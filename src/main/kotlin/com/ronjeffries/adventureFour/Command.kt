package com.ronjeffries.adventureFour

interface CommandContext {
    val magicWords: List<String>
    val ignoredNounWords: List<String>
    val directions: List<String>
}

class TestCommandContext : CommandContext {
    override val magicWords = listOf("xyzzy", "plugh", "wd40")
    override val ignoredNounWords = listOf("inventory", "look")
    override val directions = listOf(
        "n","e","s","w","north","east","south","west",
        "nw","northwest", "sw","southwest", "ne", "northeast", "se", "southeast",
        "up","dn","down")
}

class Command(val input: String, val context: CommandContext = TestCommandContext()) {
    val words = mutableListOf<String>()
    var operation = {  noun:String -> "initial operation" }
    var result: String = ""
    val verb get() = words[0]
    val noun get() = words[1]

    fun validate(): Command {
        return this
            .makeWords()
            .oneOrTwoWords()
            .goWords()
            .magicWords()
            .singleWordCommands()
            .errorIfOnlyOneWord()
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
            "take" -> take
            "go" -> go
            "say" -> say
            "inventory" -> ::inventory
            "verbError" -> verbError
            "countError" -> ::countError
            else -> commandError
        }
        return this
    }

    fun goWords(): Command {
        return substituteSingle("go", context.directions)
    }

    fun magicWords(): Command {
        return substituteSingle("say", context.magicWords)
    }

    fun singleWordCommands(): Command {
        if (words.size == 2) return this
        if (words[0] in context.ignoredNounWords) words.add("ignored")
        return this
    }

    fun substituteSingle(sub: String, singles: List<String>): Command {
        if (words.size == 2) return this
        if (words[0] in singles) words.add(0, sub)
        return this
    }

    // execution

    fun execute(): String {
        result = findOperation().operation(noun)
        return result
    }

    val commandError = {noun: String -> "command error '$input'." }
    val go = {noun: String -> "went $noun." }
    val say = {noun:String -> "said $noun." }
    val take = {noun:String -> "$noun taken."}
    fun inventory(noun: String): String = "Did inventory with '$noun'."
    val verbError = {noun: String -> "I don't understand $noun."}
    fun countError(noun: String): String = "I understand only one- and two-word commands, not '$noun'."
}