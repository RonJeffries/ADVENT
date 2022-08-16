package com.ronjeffries.adventureFour

class Game(val world: World) {
    var currentRoomName = world.rooms[0].name

    fun command(cmd: String) {
        when(cmd) {
            "s" -> move("s")
            "n" -> move("n")
            else -> {println("unknown cmd $cmd")}
        }
    }

    fun move(dir:String) {
        val room: Room = currentRoom()
        val moves = room.moves
        val move = moves.firstOrNull{it.first==dir}
       currentRoomName = move?.second ?:currentRoomName
    }

    fun currentRoom(): Room {
        return world.rooms.first{it.name == currentRoomName}
    }
}
