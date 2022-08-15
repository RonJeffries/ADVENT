package com.ronjeffries.adventureFour

class World {
    val name = "world"
    val contents = mutableListOf<Room>()

    fun room(name: String, init: Room.()->Unit) : Room {
        val room = Room(name)
        contents += room
        room.init()
        return room
    }

}
