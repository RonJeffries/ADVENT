package com.ronjeffries.adventureFour

typealias Items = MutableMap<String, Item>

class Item(val name: String) {
    var shortDesc = name
    var longDesc = name
    fun desc(short: String, long: String) {
        shortDesc = short
        longDesc = long
    }
}