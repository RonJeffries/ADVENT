package com.ronjeffries.adventureFour

data class Command(val input: String)

class Player(private val world: World, startingName: R) {
    var currentRoomName = startingName

    fun command(commandString: String): String {
        val response = world.command(Command(commandString), currentRoomName)
        currentRoomName = response.nextRoomName
        return response.resultString
    }
}
