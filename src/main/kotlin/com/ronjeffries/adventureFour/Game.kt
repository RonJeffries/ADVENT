package com.ronjeffries.adventureFour

class Game(val world: World, startingName: String) {
    val resultString: String get() = currentRoom.longDesc
    var currentRoom = world.unsafeRoomNamed(startingName)
    val currentRoomName get() = currentRoom.roomName
    val roomReferences: Set<String> get() = world.roomReferences

    fun command(cmd: String) {
        currentRoom = world.command(cmd, currentRoom)
    }
}
