package com.ronjeffries.adventureFour

class ImperativeFactory(private val lexicon: Lexicon) {

    fun fromOneWord(verb:String): Imperative = imperative(verb)
    fun fromTwoWords(verb:String, noun:String): Imperative
        = imperative(verb).setNoun(synonym(noun))
    private fun imperative(verb: String) = lexicon.translate(synonym(verb))
    private fun synonym(verb: String) = lexicon.synonym(verb)

    fun fromString(input: String): Phrase {
        val words = input.lowercase().split(" ")
        val imp = when (words.size) {
            1-> fromOneWord(words[0])
            2-> fromTwoWords(words[0], words[1])
            else -> fromTwoWords(":tooManyWords", input)
        }
        return imp.phrase
    }
}