package com.ronjeffries.adventureFour

class World {
    val name = "world"
    private val rooms = mutableMapOf<String,Room>()

    fun room(name: String, init: Room.()->Unit) : Room {
        val room = Room(name)
        rooms[name] = room
        room.init()
        return room
    }

    val roomCount get() = rooms.size

    fun hasRoomNamed(name: String): Boolean {
        return rooms.containsKey(name)
    }

    fun roomNamed(name:String) :Room {
        return rooms[name]!!
    }
}
