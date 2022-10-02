package com.ronjeffries.adventureFour

import tornadofx.singleAssign

class GameResponse(var nextRoomName:R = R.ZTestFirst) {
    var sayings = ""
    var nextRoom: Room by singleAssign()
    val resultString: String get() = sayings + nextRoom.description() +"\n"+ nextRoom.itemString()

    fun say(s:String) {
        sayings += s+"\n"
    }
}
