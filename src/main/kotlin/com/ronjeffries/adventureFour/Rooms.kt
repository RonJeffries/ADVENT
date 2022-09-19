package com.ronjeffries.adventureFour

class Rooms {
    private val rooms = mutableMapOf<String,Room>()
    val size get() = rooms.size

    fun add(room: Room) {
        rooms[room.roomName] = room
    }

    fun containsKey(name: String): Boolean  = rooms.containsKey(name)

    fun getOrDefault(name: String, default: Room): Room = rooms.getOrDefault(name, default)

    val roomReferences: Set<String> get() = (rooms.flatMap { it.value.roomReferences }).toSet()

    fun unsafeRoomNamed(name: String): Room  = rooms[name]!!
}
