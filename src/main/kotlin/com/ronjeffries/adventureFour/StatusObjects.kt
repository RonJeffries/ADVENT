package com.ronjeffries.adventureFour

class GameStatusMap {
    private val map = mutableMapOf<String, GameStatus>()
    operator fun get(name: String) = map.getOrPut(name) { GameStatus() }

    fun decrement(name: String) = this[name].decrement()
    fun increment(name: String) = this[name].increment()
    fun isFalse(name: String) = this[name].isFalse
    fun isTrue(name: String) = this[name].isTrue
    fun not(name:String) = this[name].not()
    fun set(name: String, truth: Boolean) = this[name].set(truth)
    fun set(name:String, amount:Int) = this[name].set(amount)
    fun truth(name: String): Boolean = this[name].isTrue
    fun value(name: String) = this[name].value
}

class GameStatus(var value: Int = 0) {
    val isTrue: Boolean get() = value != 0
    val isFalse: Boolean get() = value == 0
    fun increment() { value++ }
    fun decrement() { value-- }
    fun not() { value = if (isTrue) 0 else 1 }
    fun set(truth: Boolean) { value = if (truth) 1 else 0 }
    fun set(amount:Int) {value = amount}
}