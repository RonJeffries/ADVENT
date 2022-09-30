package com.ronjeffries.adventureFour

data class Command(val input: String)

class Player(private val world: World, startingName: R) {
    var currentRoom = world.unsafeRoomNamed(startingName)

    val currentRoomName get() = currentRoom.roomName
    val roomReferences: Set<R> get() = world.roomReferences

    fun command(commandString: String): String {
        val response = world.command(Command(commandString), currentRoom)
        currentRoom = response.nextRoom
        return response.resultString
    }
}
