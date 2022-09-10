package com.ronjeffries.adventureFour

class Rooms {
    private val rooms = mutableMapOf<String,Room>()
    val size get() = rooms.size
    fun names() = rooms.keys

    val roomReferences: Set<String> get() {
        return (rooms.flatMap { it.value.roomReferences }).toSet()
    }

    fun add(room: Room) {
        rooms[room.roomName] = room
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
