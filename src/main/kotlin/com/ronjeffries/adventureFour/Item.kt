package com.ronjeffries.adventureFour

//typealias Items = MutableMap<String, Item>
typealias ItemMap = MutableMap<String, Item>

class Items(private val map: ItemMap = mutableMapOf<String, Item>()) {
    fun size() = map.keys.size
    val values = map.values

    fun add(item: Item) {
        map[item.name] = item
    }

    fun contains(item: String): Boolean {
        return map.containsKey(item)
    }

    fun getItem(name: String): Item? {
        return map[name]
    }

    fun remove(key:String): Item? {
        return map.remove(key)
    }
}

class Item(val name: String) {
    var shortDesc = name
    var longDesc = name
    fun desc(short: String, long: String = short) {
        shortDesc = short
        longDesc = long
    }
}