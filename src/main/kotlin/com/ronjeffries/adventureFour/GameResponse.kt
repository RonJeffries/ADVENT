package com.ronjeffries.adventureFour

import tornadofx.singleAssign

data class GameResponse(val name:String="GameResponse") {
    val resultString: String get() = sayings + nextRoom.longDesc +"\n"+ nextRoom.itemString()
    var sayings = ""
    lateinit var nextRoomName: String
    var nextRoom: Room by singleAssign<Room>()

    fun say(s:String) {
        sayings += s+"\n"
    }
}
