package com.ronjeffries.adventureFour

fun world(details: World.()->Unit): World{
    val world = World()
    world.details()
    return world
}


class World {
    var actions: Actions = makeActions()
    val lexicon = makeLexicon()

    private fun makeLexicon(): Lexicon {
        return Lexicon(makeSynonyms(), makeVerbs())
    }

    private fun makeSynonyms(): Synonyms {
        return Synonyms( mutableMapOf(
            "east" to "e",
            "north" to "n",
            "west" to "w",
            "south" to "s").withDefault { it }
        )
    }

    private fun makeVerbs(): Verbs {
        return Verbs(mutableMapOf(
            "go" to Phrase("go", "irrelevant"),
            "e" to Phrase("go", "e"),
            "w" to Phrase("go", "w"),
            "n" to Phrase("go", "n"),
            "s" to Phrase("go", "s"),
            "say" to Phrase("say", "irrelevant"),
            "xyzzy" to Phrase("say", "xyzzy"),
            "wd40" to Phrase("say","wd40"),
        ).withDefault { Phrase(it, "none")})
    }

    private fun makeActions(): Actions {
        return Actions(mutableMapOf(
            Phrase("go") to { imp: Imperative -> imp.room.move(imp, imp.world) },
            Phrase("say") to { imp: Imperative -> imp.room.castSpell(imp, imp.world) },
            Phrase("take") to { imp: Imperative -> imp.room.take(imp, imp.world) },
            Phrase("inventory") to { imp: Imperative -> imp.world.showInventory() },
            Phrase() to { imp: Imperative -> imp.room.unknown(imp, imp.world) }
        ))
    }

    val flags = GameStatusMap()
    private val inventory = mutableSetOf<String>()
    val name = "world"
    val resultString: String get() = response.resultString
    private val rooms = Rooms()
    var response: GameResponse = GameResponse()

    val roomCount get() = rooms.size
    val roomReferences: Set<String> get() = rooms.roomReferences

    fun flag(name: String) = flags.get(name)

    fun room(name: String, details: Room.()->Unit) : Room {
        val room = Room(name)
        rooms.add(room)
        room.details()
        return room
    }

    fun addToInventory(item:String) {
        inventory += item
    }

    fun inventoryHas(item: String): Boolean {
        return inventory.contains(item)
    }

    fun hasRoomNamed(name: String): Boolean {
        return rooms.containsKey(name)
    }

    fun roomNamedOrDefault(name: String, default: Room) :Room {
        return rooms.getOrDefault(name, default)
    }

    fun unsafeRoomNamed(name: String): Room {
        return rooms.unsafeRoomNamed(name)
    }

    fun command(cmd: Command, currentRoom: Room, newResponse: GameResponse): GameResponse {
        response = GameResponse()
        currentRoom.command(cmd, this)
        response.nextRoom = roomNamedOrDefault(response.nextRoomName, currentRoom)
        return response
    }

    fun say(s: String) {
        response.say(s)
    }

    private fun showInventory() {
        say( inventory.joinToString(prefix="You have ", separator=", ", postfix=".\n") )
    }
}

