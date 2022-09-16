package com.ronjeffries.adventureFour

data class Command(val input: String)

class Player(private val world: World, startingName: String) {
    var currentRoom = world.unsafeRoomNamed(startingName)

    val currentRoomName get() = currentRoom.roomName
    val roomReferences: Set<String> get() = world.roomReferences

    fun command(commandString: String): String {
        val response = world.command(Command(commandString), currentRoom)
        currentRoom = response.nextRoom
        return response.resultString
    }
}
