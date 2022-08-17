package com.ronjeffries.adventureFour

fun world(details: World.()->Unit): World{
    val world = World()
    world.details()
    return world
}

class World {
    val name = "world"
    private val rooms = mutableMapOf<String,Room>()

    fun room(name: String, details: Room.()->Unit) : Room {
        val room = Room(name)
        rooms[name] = room
        room.details()
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
