package com.ronjeffries.adventureFour

class Player(val world: World, startingName: String) {
    val resultString: String get() = response.resultString
    var currentRoom = world.unsafeRoomNamed(startingName)
    val currentRoomName get() = currentRoom.roomName
    val roomReferences: Set<String> get() = world.roomReferences
    var response: GameResponse = GameResponse()

    fun command(commandString: String) {
        response = GameResponse()
        var command = Command(commandString)
        world.command(command, currentRoom, response)
        currentRoom = response.nextRoom
    }
}
