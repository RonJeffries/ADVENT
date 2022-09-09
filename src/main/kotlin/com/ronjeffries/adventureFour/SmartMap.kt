package com.ronjeffries.adventureFour

class SmartMap<K,V>(private val global: MutableMap<K,V>, local: MutableMap<K,V>) {
    private val safeLocal = local.withDefault { key: K -> getGlobalValue(key) }
    fun getValue(key: K): V = safeLocal.getValue(key)
    fun getGlobalValue(key: K): V = global.getValue(key)
    fun clearLocal() {
        safeLocal.clear()
    }
}