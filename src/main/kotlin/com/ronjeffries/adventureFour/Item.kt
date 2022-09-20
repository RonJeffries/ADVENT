package com.ronjeffries.adventureFour

//typealias Items = MutableMap<String, Item>
typealias ItemMap = MutableMap<String, Item>

class Items(private val map: ItemMap = mutableMapOf<String, Item>()) {
    val size get() = map.keys.size

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

    fun asFound(): String
        = map.values.joinToString(separator = "") { it.asFound() }

    fun contains(item: String): Boolean = map.containsKey(item)

    fun moveItemTo(name: String, target: Items): Boolean
        = remove(name)?.also {target.add(it)} != null

    private fun remove(key:String): Item? = map.remove(key)

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