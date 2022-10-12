package com.ronjeffries.adventureFour

class GameResponse(private val player: Player) {
    var nextRoomName: R = player.currentRoomName
        private set

    val resultString: String get() = buildString {
        append(sayings)
        append(nextRoomName.description())
        append("\n")
        append(nextRoomName.itemString())
    }
    var sayings = ""

    fun goToPriorRoom() = moveToRoomNamed(player.priorRoomName)

    fun moveToRoomNamed(roomName: R) {
        nextRoomName = roomName
    }

    fun say(s:String) {
        sayings += s+"\n"
    }
}
