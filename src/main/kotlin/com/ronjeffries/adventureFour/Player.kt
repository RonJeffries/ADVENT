package com.ronjeffries.adventureFour

class Player(val world: World, startingName: String) {
    var response: GameResponse = GameResponse()
    var currentRoom = world.unsafeRoomNamed(startingName)
    val roomReferences: Set<String> get() = world.roomReferences

    val currentRoomName get() = currentRoom.roomName
    val resultString: String get() = response.resultString

    fun command(commandString: String) {
        response = GameResponse()
        var command = Command(commandString)
        world.command(command, currentRoom, response)
        currentRoom = response.nextRoom
    }
}
