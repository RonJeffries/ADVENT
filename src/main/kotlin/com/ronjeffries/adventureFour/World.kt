package com.ronjeffries.adventureFour

fun world(details: World.()->Unit): World{
    val world = World()
    world.details()
    return world
}


class World {
    var testNoun: String = ""
    var testVerb: String = ""
    val lexicon = makeLexicon()

    private fun makeLexicon(): Lexicon {
        return Lexicon(makeSynonyms(), makeVerbs(), makeActions())
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
            "go" to SimpleImperative("go", "irrelevant"),
            "e" to SimpleImperative("go", "e"),
            "w" to SimpleImperative("go", "w"),
            "n" to SimpleImperative("go", "n"),
            "s" to SimpleImperative("go", "s"),
            "say" to SimpleImperative("say", "irrelevant"),
            "xyzzy" to SimpleImperative("say", "xyzzy"),
            "wd40" to SimpleImperative("say","wd40"),
        ).withDefault { (SimpleImperative(it, "none"))})
    }

    private fun makeActions(): Actions {
        return Actions(mutableMapOf(
            "go" to { imp: Imperative -> imp.room.move(imp, imp.world) },
            "say" to { imp: Imperative -> imp.room.castSpell(imp, imp.world) },
            "take" to { imp: Imperative -> imp.room.take(imp, imp.world) },
            "inventory" to { imp: Imperative -> imp.room.inventory(imp, imp.world) },
        ).withDefault {
            { imp: Imperative -> imp.room.unknown(imp, imp.world) }
        }
        )
    }

    val flags = GameStatusMap()
    val inventory = mutableSetOf<String>()
    val name = "world"
    val resultString: String get() = response.resultString
    private val rooms = Rooms()
    lateinit var response: GameResponse

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

    fun command(cmd: Command, currentRoom: Room, newResponse: GameResponse) {
        response = newResponse
        currentRoom.command(cmd, this)
        response.nextRoom = roomNamedOrDefault(response.nextRoomName, currentRoom)
    }

    fun say(s: String) {
        response.say(s)
    }

    fun showInventory() {
        say( inventory.joinToString(prefix="You have ", separator=", ", postfix=".\n") )
    }
}
