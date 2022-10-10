package com.ronjeffries.adventureFour

class GameResponse(startingRoomName:R = R.Z_FIRST) {
    var nextRoomName = startingRoomName
        get() = field // used only by tests

    var player: Player? = null

    val resultString: String get() = sayings + nextRoomName.description() + "\n"+ nextRoomName.itemString()
    var sayings = ""

    fun goToPriorRoom() {
        val name = player?.priorRoomName
        if (name != null ) moveToRoomNamed(name)
    }

    fun moveToRoomNamed(roomName: R) {
        nextRoomName = roomName
    }

    fun say(s:String) {
        sayings += s+"\n"
    }

    fun definePlayer(currentPlayer: Player) {
        this.player = currentPlayer
    }
}
