package com.ronjeffries.adventureFour

class World {
    val name = "world"
    val rooms = mutableListOf<Room>()

    fun room(name: String, init: Room.()->Unit) : Room {
        val room = Room(name)
        rooms += room
        room.init()
        return room
    }

    val roomCount get() = rooms.size

    fun hasRoomNamed(name: String): Boolean {
        return rooms.any { it.name == name}
    }

    fun roomNamed(name:String) :Room {
        return rooms.first {it.name == name}
    }
}
