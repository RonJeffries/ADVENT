package com.ronjeffries.adventureFour

class Command(val input: String) {
    val words = mutableListOf<String>()
    var operation = this::commandError
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
            "take" -> ::take
            "go" -> ::go
            "say" -> ::say
            "inventory" -> ::inventory
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
        val magicWords = listOf("xyzzy", "plugh", "wd40")
        return substituteSingle("say", magicWords)
    }

    fun singleWordCommands(): Command {
        if (words.size == 2) return this
        val ignoredNounWords = listOf("inventory", "look")
        if (words[0] in ignoredNounWords) words.add("ignored")
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

    fun commandError(noun: String) : String = "command error '$input'."
    fun go(noun: String): String = "went $noun."
    fun say(noun:String): String = "said $noun."
    fun take(noun:String): String = "$noun taken."
    fun inventory(noun: String): String = "Did inventory with '$noun'."
    fun verbError(noun: String): String = "I don't understand $noun."
    fun countError(noun: String): String = "I understand only one- and two-word commands, not '$noun'."
}