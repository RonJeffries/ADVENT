package com.ronjeffries.adventureFour

class GameResponse(startingRoomName:R = R.Z_FIRST) {
    var nextRoomName = startingRoomName
        get() = field // used only by tests

    val resultString: String get() = sayings + nextRoomName.description() + "\n"+ nextRoomName.itemString()
    var sayings = ""

    fun moveToRoomNamed(roomName: R) {
        nextRoomName = roomName
    }

    fun say(s:String) {
        sayings += s+"\n"
    }
}
