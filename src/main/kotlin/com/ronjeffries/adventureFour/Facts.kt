package com.ronjeffries.adventureFour

// TODO maybe make flags and numbers separate instead of this cuteness?

class Facts {
    private val map = mutableMapOf<String, Fact>()
    operator fun get(name: String) = map.getOrPut(name) { Fact() }

    fun increment(name: String): Unit           = this[name].increment()
    fun isFalse(name: String): Boolean          = this[name].isFalse
    fun isTrue(name: String): Boolean           = this[name].isTrue
    fun decrement(name: String): Unit           = this[name].decrement()
    fun not(name: String): Unit                 = this[name].not()
    fun set(name: String, truth: Boolean): Unit = this[name].set(truth)
    fun set(name: String, amount: Int): Unit    = this[name].set(amount)
    fun truth(name: String): Boolean            = this[name].isTrue
    fun value(name: String): Int                = this[name].value
}

class Fact(var value: Int = 0) {
    val isTrue: Boolean get()  = value != 0
    val isFalse: Boolean get() = value == 0
    fun decrement()            { value -= 1 }
    fun increment()            { value += 1 }
    fun not()                  { value = if (isTrue) 0 else 1 }
    fun set(amount:Int)        { value = amount }
    fun set(truth: Boolean)    { value = if (truth) 1 else 0 }
    val truth: Boolean get()   = value != 0
}