package com.ronjeffries.adventureFour

class BooleanStatusMap {
    val map = mutableMapOf<String, BooleanStatus>()

    fun item(name:String): BooleanStatus {
        return map.getOrPut(name) { BooleanStatus() }
    }
}

class BooleanStatus(var value: Boolean = false) {
    val isTrue: Boolean get() = value
    val isFalse: Boolean get() = !value

    fun not() {
        value = !value
    }

    fun set(truth: Boolean) {
        value = truth
    }
}