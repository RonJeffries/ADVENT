package com.ronjeffries.adventureFour

class Rooms {
    private val rooms = mutableMapOf<String,Room>()

    fun add(room: Room) {
        rooms.put(room.name, room)
    }

    fun getOrDefault(name: String, default: Room): Room {
        return rooms.getOrDefault(name, default)
    }
}
