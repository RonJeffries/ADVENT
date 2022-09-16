package com.ronjeffries.adventureFour

import tornadofx.singleAssign

class GameResponse(var nextRoomName:String = "") {
    var sayings = ""
    var nextRoom: Room by singleAssign()
    val resultString: String get() = sayings + nextRoom.longDesc +"\n"+ nextRoom.itemString()

    fun say(s:String) {
        sayings += s+"\n"
    }
}
