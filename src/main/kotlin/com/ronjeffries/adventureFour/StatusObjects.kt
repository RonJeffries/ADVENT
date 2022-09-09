package com.ronjeffries.adventureFour

class GameStatusMap {
    val map = mutableMapOf<String, GameStatus>()

    fun get(name:String): GameStatus {
        return map.getOrPut(name) { GameStatus() }
    }
}

class GameStatus(var value: Int = 0) {
    val isTrue: Boolean get() = value != 0
    val isFalse: Boolean get() = value == 0
    fun increment() { value++ }
    fun not() { value = if (isTrue) 0 else 1 }
    fun set(truth: Boolean) { value = if (truth) 1 else 0 }
}