package com.ronjeffries.adventureFour


typealias GoTarget = Pair<R, (World)->Boolean>

enum class R {
    Spring, Wellhouse, Woods12, Woods9, Woods6, WoodsS,  WoodsNearCave, CaveEntrance,
    LowCave, Clearing, Y2,
    Z_FIRST, Z_SECOND, Z_PALACE, Z_TREASURE,
    ;

    var room = Room(this)
        private set

    fun freshRoom(): Room {
        room = Room(this)
        return room
    }
}

enum class D {
    North, South, East, West,
    Northwest, Southwest,Northeast,Southeast,
    Up, Down;

    companion object {
        fun executeIfDirectionExists(desired: String, function: (D) -> Unit) {
            valuesMatching(desired)?.let { function(it) }
        }

        private fun valuesMatching(desired: String): D? {
            return values().find {it.name.equals(desired, ignoreCase = true)}
        }
    }
}

fun world(details: World.()->Unit): World{
    val world = World()
    world.details()
    return world
}


class World( val actions: IActions = Actions()) :IActions by actions {
    init {
        makeActions()
    }
    val lexicon = makeLexicon()
    val flags = GameStatusMap()
    val inventory: Items = Items()
    val name = "world"
    var response: GameResponse = GameResponse()


    // DSL

    fun room(name: R, details: Room.() -> Unit): Room {
        val room = name.freshRoom()
        room.details()
        return room
    }

// Game Play

    fun addToInventory(item: Item) {
        inventory.add(item)
    }

    fun command(cmd: Command, currentRoom: Room): GameResponse {
        response = GameResponse(currentRoom.roomName)
        currentRoom.command(cmd, this)
//        response.nextRoom = response.nextRoomName.room
        return response
    }

    fun inventoryHas(item: String): Boolean = inventory.contains(item)

    fun inventorySetInformation(item: String, property: String) {
        inventory.setInformation(item, property)
    }

    fun say(s: String) {
        response.say(s)
    }

    fun yes(s:String): Boolean {
        say(s)
        return true
    }

    fun no(s:String): Boolean {
        say(s)
        return false
    }

    private fun showInventory() {
        say(inventory.asCarried())
    }

// creation utilities

    private fun makeLexicon(): Lexicon = Lexicon(makeSynonyms(), makeVerbs())

    private fun makeSynonyms(): Synonyms {
        return Synonyms(mutableMapOf(
            "e" to "east", "ne" to "northeast",
            "n" to "north", "se" to "southeast",
            "w" to "west", "nw" to "northwest",
            "s" to "south", "sw" to "southwest"
        ).withDefault { it }
        )
    }

    private fun makeVerbs(): Verbs {
        return Verbs(mutableMapOf(
            "go" to Phrase("go", "irrelevant"),
            "east" to Phrase("go", "east"),
            "west" to Phrase("go", "west"),
            "north" to Phrase("go", "north"),
            "south" to Phrase("go", "south"),
            "northwest" to Phrase("go", "northwest"),
            "southwest" to Phrase("go", "southwest"),
            "northeast" to Phrase("go", "northeast"),
            "southeast" to Phrase("go", "southeast"),
            "say" to Phrase("say", "irrelevant"),
            "look" to Phrase("look", "around"),
            "xyzzy" to Phrase("say", "xyzzy"),
            "wd40" to Phrase("say", "wd40"),
        ).withDefault { Phrase(it, "none") })
    }

    private fun makeActions() {
        with(actions) {
            add(Phrase("go")) { imp: Imperative -> imp.room.move(imp, imp.world) }
            add(Phrase("say", "wd40")) { imp: Imperative ->
                imp.world.say("Very slick, but there's nothing to lubricate here.")
            }
            add(Phrase("say")) { imp: Imperative ->
                imp.world.say("Nothing happens here!")
            }
            add(Phrase("take")) { imp: Imperative -> imp.room.take(imp, imp.world) }
            add(Phrase("inventory")) { imp: Imperative -> imp.world.showInventory() }
            add(Phrase("look")) { imp: Imperative -> imp.room.look() }
            add(Phrase()) { imp: Imperative -> imp.room.unknown(imp, imp.world) }
        }
    }

}

