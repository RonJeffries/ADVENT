package com.ronjeffries.adventureFour

class PhraseFactory(private val lexicon: Lexicon) {

    fun fromOneWord(verb:String): Phrase = getVerbPhrase(verb)
    fun fromTwoWords(verb:String, noun:String): Phrase
        = getVerbPhrase(verb).setNoun(synonym(noun))
    private fun getVerbPhrase(verb: String): Phrase = lexicon.translate(synonym(verb))
    private fun synonym(verb: String) = lexicon.synonym(verb)

    fun fromString(input: String): Phrase {
        val words = input.lowercase().split(" ")
        val phrase = when (words.size) {
            1-> fromOneWord(words[0])
            2-> fromTwoWords(words[0], words[1])
            else -> fromTwoWords(":tooManyWords", input)
        }
        return phrase
    }
}