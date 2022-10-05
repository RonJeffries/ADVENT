package com.ronjeffries.adventureFour

class Facts {
    private val map = mutableMapOf<String, Fact>()
    operator fun get(name: String) = map.getOrPut(name) { Fact() }

    fun increment(name: String): Int            = this[name].increment()
    fun isFalse(name: String): Boolean          = this[name].isFalse
    fun isTrue(name: String): Boolean           = this[name].isTrue
    fun decrement(name: String): Int            = this[name].decrement()
    fun not(name: String): Unit                 = this[name].not()
    fun set(name: String, truth: Boolean): Unit = this[name].set(truth)
    fun set(name: String, amount: Int): Unit    = this[name].set(amount)
    fun truth(name: String): Boolean            = this[name].isTrue
    fun value(name: String): Int                = this[name].value
}

class Fact(var value: Int = 0) {
    val isTrue: Boolean get()  = value != 0
    val isFalse: Boolean get() = value == 0
    fun decrement()            = value--
    fun increment()            = value++
    fun not()                  { value = if (isTrue) 0 else 1 }
    fun set(amount:Int)        { value = amount }
    fun set(truth: Boolean)    { value = if (truth) 1 else 0 }
}