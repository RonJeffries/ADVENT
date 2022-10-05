package com.ronjeffries.adventureFour

class GameResponse(startingRoomName:R = R.Z_FIRST) {
    var nextRoomName = startingRoomName
        get() = field // used only by tests
    var nextRoom: Room = nextRoomName.room
        get() = field

    val resultString: String get() = sayings + nextRoom.description() +"\n"+ nextRoom.itemString()
    var sayings = ""

    fun moveToRoomNamed(roomName: R) {
        nextRoomName = roomName
        nextRoom = roomName.room
    }

    fun say(s:String) {
        sayings += s+"\n"
    }
}
