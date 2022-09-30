package com.ronjeffries.adventureFour

class Rooms {
    private val rooms = mutableMapOf<R,Room>()
    val size get() = rooms.size

    fun add(room: Room) {
        rooms[room.roomName] = room
    }

    fun containsKey(name: R): Boolean  = rooms.containsKey(name)

    fun getOrDefault(name: R, default: Room): Room = rooms.getOrDefault(name, default)

    val roomReferences: Set<R> get() = (rooms.flatMap { it.value.roomReferences }).toSet()

    fun unsafeRoomNamed(name: R): Room  = rooms[name]!!
}
