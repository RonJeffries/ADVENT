package com.ronjeffries.adventureFour

class GameResponse(private val player: Player) {
    var nextRoomName = player.currentRoomName

    val resultString: String get() = sayings + nextRoomName.description() + "\n"+ nextRoomName.itemString()
    var sayings = ""

    fun goToPriorRoom() {
         moveToRoomNamed(player.priorRoomName)
    }

    fun moveToRoomNamed(roomName: R) {
        nextRoomName = roomName
    }

    fun say(s:String) {
        sayings += s+"\n"
    }
}
