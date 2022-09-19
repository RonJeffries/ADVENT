package com.ronjeffries.adventureFour

//typealias Items = MutableMap<String, Item>
typealias ItemMap = MutableMap<String, Item>

class Items(private val map: ItemMap = mutableMapOf<String, Item>()) {
    fun size() = map.keys.size

    fun add(item: Item) {
        map[item.name] = item
    }

    fun asCarried(): String {
        return map.values.joinToString(
            prefix = "You have ",
            transform = { it.shortDesc },
            separator = ", ",
            postfix = ".\n"
        )
    }

    fun asFound(): String {
        return map.values.joinToString(separator = "") { it.asFound() }
    }

    fun contains(item: String): Boolean {
        return map.containsKey(item)
    }

    fun moveItemTo(name: String, target: Items): Boolean {
        val maybeItem: Item? = remove(name)
        return when (maybeItem) {
            null -> false
            else -> {
                target.add(maybeItem)
                return true
            }
        }
    }

    private fun remove(key:String): Item? {
        return map.remove(key)
    }
}

class Item(val name: String) {
    var shortDesc = name
    var longDesc = name
    fun asFound() = "You find $shortDesc.\n"
    fun desc(short: String, long: String = short) {
        shortDesc = short
        longDesc = long
    }
}