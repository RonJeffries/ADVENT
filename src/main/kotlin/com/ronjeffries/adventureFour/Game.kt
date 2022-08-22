package com.ronjeffries.adventureFour

class Game(val world: World, startingName: String) {
    val resultString: String get() = currentRoom.longDesc
    var currentRoom = world.unsafeRoomNamed(startingName)
    val currentRoomName get() = currentRoom.name
    val roomReferences: Set<String> get() = world.roomReferences

    fun command(cmd: String) {
        val name = when(cmd) {
            "s" -> currentRoom.move("s")
            "n" -> currentRoom.move("n")
            "xyzzy" -> currentRoom.move("xyzzy")
            else -> "unknown cmd $cmd"
        }
        currentRoom = world.roomNamedOrDefault(name,currentRoom)
    }
}
