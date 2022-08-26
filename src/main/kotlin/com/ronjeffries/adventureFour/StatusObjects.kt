package com.ronjeffries.adventureFour

class BooleanStatusMap {
    val map = mutableMapOf<String, BooleanStatus>()

    fun get(name:String): BooleanStatus {
        return map.getOrPut(name) { BooleanStatus() }
    }
}

class BooleanStatus(var value: Int = 0) {
    val isTrue: Boolean get() = value != 0
    val isFalse: Boolean get() = value == 0
    fun increment() { value++ }
    fun not() { value = if (isTrue) 0 else 1 }
    fun set(truth: Boolean) { value = if (truth) 1 else 0 }
    fun set(number: Int) { value = number }
}