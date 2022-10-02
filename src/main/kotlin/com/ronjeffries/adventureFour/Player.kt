package com.ronjeffries.adventureFour

data class Command(val input: String)

class Player(private val world: World, startingName: R) {
    var currentRoom = startingName.room

    val currentRoomName get() = currentRoom.roomName

    fun command(commandString: String): String {
        val response = world.command(Command(commandString), currentRoom)
        currentRoom = response.nextRoom
        return response.resultString
    }
}
