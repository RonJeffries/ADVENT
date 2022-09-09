package com.ronjeffries.adventureFour

class SmartMap<K,V>(private val global: MutableMap<K,V>, local: MutableMap<K,V>) {
    private val local = local.withDefault { key: K -> getGlobalValue(key) }
    fun getValue(key: K): V = local.getValue(key)
    fun getGlobalValue(key: K): V = global.getValue(key)
    fun clearLocal() {
        local.clear()
    }
}