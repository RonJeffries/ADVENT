package com.ronjeffries.adventureFour

class Game(val world: World, startingName: String) {
    var currentRoomName = startingName

    fun command(cmd: String) {
        when(cmd) {
            "s" -> move("s")
            "n" -> move("n")
            "xyzzy" -> move("xyzzy")
            else -> {println("unknown cmd $cmd")}
        }
    }

    fun move(dir:String) {
        currentRoomName = currentRoom().moves.firstOrNull{it.first==dir}?.second ?:currentRoomName
    }

    fun currentRoom(): Room {
        return world.roomNamed(currentRoomName)
    }
}
