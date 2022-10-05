package com.ronjeffries.adventureFour

class GameStatusMap {
    private val map = mutableMapOf<String, GameStatus>()

    operator fun get(s: String) = map.getOrPut(s) { GameStatus() }
//    fun get(name:String): GameStatus = map.getOrPut(name) { GameStatus() }
    private fun fetch(name: String) = map.getOrPut(name) { GameStatus()}
    fun truth(name: String): Boolean = fetch(name).isTrue
    fun not(name:String) = fetch(name).not()
    fun value(name: String) = fetch(name).value
    fun increment(name: String) = fetch(name).increment()
    fun decrement(name: String) = fetch(name).decrement()
    fun set(name: String, truth: Boolean) = fetch(name).set(truth)
    fun set(name:String, amount:Int) = fetch(name).set(amount)
    fun isTrue(name: String) = fetch(name).isTrue
    fun isFalse(name: String) = fetch(name).isFalse
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