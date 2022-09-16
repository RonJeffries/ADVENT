package com.ronjeffries.adventureFour

data class Command(val input: String)

class Player(private val world: World, startingName: String) {
    private var response: GameResponse = GameResponse()
    var currentRoom = world.unsafeRoomNamed(startingName)
    val roomReferences: Set<String> get() = world.roomReferences

    val currentRoomName get() = currentRoom.roomName
    val resultString: String get() = response.resultString

    fun command(commandString: String) {
        response = world.command(Command(commandString), currentRoom)
        currentRoom = response.nextRoom
    }
}
