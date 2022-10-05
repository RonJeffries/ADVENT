package com.ronjeffries.adventureFour

class GameStatusMap {
    private val map = mutableMapOf<String, GameStatus>()

    operator fun get(s: String) = map.getOrPut(s) { GameStatus() }
//    fun get(name:String): GameStatus = map.getOrPut(name) { GameStatus() }
    fun truth(name: String): Boolean = map.getOrPut(name){ GameStatus() }.isTrue
    fun not(name:String) = map.getOrPut(name){ GameStatus() }.not()
}

class GameStatus(var value: Int = 0) {
    val isTrue: Boolean get() = value != 0
    val isFalse: Boolean get() = value == 0
    fun increment() { value++ }
    fun not() { value = if (isTrue) 0 else 1 }
    fun set(truth: Boolean) { value = if (truth) 1 else 0 }
    fun set(amount:Int) {value = amount}
}