package com.ronjeffries.adventureFour

import tornadofx.singleAssign

data class GameResponse(val name:String="GameResponse") {
    var sayings = ""
    lateinit var nextRoomName: String
    var nextRoom: Room by singleAssign<Room>()
    val resultString: String get() = sayings + nextRoom.longDesc +"\n"+ nextRoom.itemString()

    fun say(s:String) {
        sayings += s+"\n"
    }
}
