package com.ronjeffries.adventureFour

fun world(details: World.()->Unit): World{
    val world = World()
    world.details()
    return world
}


class World {
    val flags = GameStatusMap()
    val inventory = mutableSetOf<String>()
    val name = "world"
    val resultString: String get() = response.resultString
    private val rooms = Rooms()
    var response: GameResponse = GameResponse()

    val roomCount get() = rooms.size
    val roomReferences: Set<String> get() = rooms.roomReferences

    fun flag(name: String) = flags.get(name)

    fun room(name: String, details: Room.()->Unit) : Room {
        val room = Room(name)
        rooms.add(room)
        room.details()
        return room
    }

    fun take(item:String) {
        inventory += item
    }

    fun inventoryHas(item: String): Boolean {
        return inventory.contains(item)
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

    fun command(cmd: String, currentRoom: Room): Room {
        response = GameResponse()
        currentRoom.command(cmd, this)
        response.nextRoom = roomNamedOrDefault(response.nextRoomName, currentRoom)
        return response.nextRoom
    }

    fun say(s: String) {
        response.say(s)
    }
}
