package com.ronjeffries.adventureFour

interface CommandContext {
    val magicWords: List<String>
    val ignoredNounWords: List<String>
    val directions: List<String>
    val operations: Map<String,(Command)->String>
}

class TestCommandContext : CommandContext {
    override val magicWords = listOf("xyzzy", "plugh", "wd40")
    override val ignoredNounWords = listOf("inventory", "look")
    override val directions = listOf(
        "n","e","s","w","north","east","south","west",
        "nw","northwest", "sw","southwest", "ne", "northeast", "se", "southeast",
        "up","dn","down")
    override val operations: Map<String, (Command) -> String> = mutableMapOf(
        "go" to {command: Command -> "went ${command.noun}." },
        "say" to {command:Command -> "said ${command.noun}." },
        "take" to {command:Command -> "${command.noun} taken."},
        "inventory" to {command: Command ->  "Did inventory with '${command.noun}'."},
        "verbError" to {command: Command -> "I don't understand ${command.noun}."},
        "countError" to {command: Command ->  "I understand only one- and two-word commands, not '${command.noun}'."},
        "commandError" to {command: Command -> "command error '${command.input}'." }
    )
}

class Command(val input: String, val context: CommandContext = TestCommandContext()) {
    val words = mutableListOf<String>()
    var operation = {  command: Command -> "initial operation" }
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
        operation = context.operations.get(verb)!!
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
        result = findOperation().operation(this)
        return result
    }
}