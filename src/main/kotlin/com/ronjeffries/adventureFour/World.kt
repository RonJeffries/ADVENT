package com.ronjeffries.adventureFour

fun world(details: World.()->Unit): World{
    val world = World()
    world.details()
    return world
}

class World {
    val name = "world"
    private val rooms = Rooms()

    val roomCount get() = rooms.size
    val roomReferences: Set<String> get() = rooms.roomReferences

    fun room(name: String, details: Room.()->Unit) : Room {
        val room = Room(name)
        rooms.add(room)
        room.details()
        return room
    }

    fun hasRoomNamed(name: String): Boolean {
        return rooms.containsKey(name)
    }

    fun roomNamedOrDefault(name: String, default: Room) :Room {
        return rooms.getOrDefault(name, default)
    }

    fun unsafeRoomNamed(name: String): Room {
        return rooms.unsafeRoomNamed(name)
    }
}
