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
}
