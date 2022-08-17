package com.ronjeffries.adventureFour

class Game(val world: World, startingName: String) {
    var currentRoom = world.roomNamed(startingName)
    val currentRoomName get() = currentRoom.name

    fun command(cmd: String) {
        when(cmd) {
            "s" -> move("s")
            "n" -> move("n")
            "xyzzy" -> move("xyzzy")
            else -> {println("unknown cmd $cmd")}
        }
    }

    fun move(dir:String) {
        val name = currentRoom.moves.firstOrNull{it.first==dir}?.second ?:currentRoom.name
        currentRoom = world.roomNamedOrDefault(name,currentRoom)
    }
}
