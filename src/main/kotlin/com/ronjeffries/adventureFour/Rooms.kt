package com.ronjeffries.adventureFour

class Rooms {
    private val rooms = mutableMapOf<String,Room>()
    val size get() = rooms.size

    val roomReferences: Set<String> get() {
        val result = mutableSetOf<String>()
        rooms.forEach { (_, room) -> result += room.roomReferences }
        return result
    }

    fun add(room: Room) {
        rooms[room.name] = room
    }

    fun getOrDefault(name: String, default: Room): Room {
        return rooms.getOrDefault(name, default)
    }

    fun containsKey(name: String): Boolean {
        return rooms.containsKey(name)
    }

    fun unsafeRoomNamed(name: String): Room {
        return rooms[name]!!
    }
}
