package com.ronjeffries.adventureFour

import tornadofx.singleAssign

class GameResponse {
    var sayings = ""
    lateinit var nextRoomName: String //TODO init in constructor?
    var nextRoom: Room by singleAssign()
    val resultString: String get() = sayings + nextRoom.longDesc +"\n"+ nextRoom.itemString()

    fun say(s:String) {
        sayings += s+"\n"
    }
}
