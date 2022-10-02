package com.ronjeffries.adventureFour


typealias GoTarget = Pair<R, (World)->Boolean>

enum class R {
    Spring, Wellhouse, Woods, WoodsNearCave, CaveEntrance,
    Clearing, Y2,
    ZTestFirst, ZTestSecond, ZTestPalace, ZTestTreasure,
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


class World {
    var actions: Actions = makeActions()
    val lexicon = makeLexicon()
    val flags = GameStatusMap()
    val inventory: Items = Items()
    val name = "world"
    var response: GameResponse = GameResponse()

// creation utilities

    private fun makeLexicon(): Lexicon = Lexicon(makeSynonyms(), makeVerbs())

    private fun makeSynonyms(): Synonyms {
        return Synonyms( mutableMapOf(
            "e" to "east",
            "n" to "north",
            "w" to "west",
            "s" to "south").withDefault { it }
        )
    }

    private fun makeVerbs(): Verbs {
        return Verbs(mutableMapOf(
            "go" to Phrase("go", "irrelevant"),
            "east" to Phrase("go", "east"),
            "west" to Phrase("go", "west"),
            "north" to Phrase("go", "north"),
            "south" to Phrase("go", "south"),
            "e" to Phrase("go", "east"),
            "w" to Phrase("go", "west"),
            "n" to Phrase("go", "north"),
            "s" to Phrase("go", "south"),
            "nw" to Phrase("go", "northwest"),
            "ne" to Phrase("go", "northeast"),
            "sw" to Phrase("go", "southwest"),
            "se" to Phrase("go", "southeast"),
            "say" to Phrase("say", "irrelevant"),
            "look" to Phrase("look", "around"),
            "xyzzy" to Phrase("say", "xyzzy"),
            "wd40" to Phrase("say","wd40"),
        ).withDefault { Phrase(it, "none")})
    }

    private fun makeActions(): Actions {
        return Actions(mutableMapOf(
            Phrase("go") to { imp: Imperative -> imp.room.move(imp, imp.world) },
            Phrase("say", "wd40") to { imp: Imperative ->
                imp.world.say("Very slick, but there's nothing to lubricate here.")
            },
            Phrase("say") to { imp: Imperative ->
                imp.world.say("Nothing happens here!") },
            Phrase("take") to { imp: Imperative -> imp.room.take(imp, imp.world) },
            Phrase("inventory") to { imp: Imperative -> imp.world.showInventory() },
            Phrase("look") to { imp: Imperative-> imp.room.look()},
            Phrase() to { imp: Imperative -> imp.room.unknown(imp, imp.world) }
        ))
    }

// DSL

    fun room(name: R, details: Room.()->Unit) : Room {
        val room = name.freshRoom()
        room.details()
        return room
    }

// Game Play

    fun addToInventory(item:Item) {
        inventory.add(item)
    }

    fun command(cmd: Command, currentRoom: Room): GameResponse {
        response = GameResponse(currentRoom.roomName)
        currentRoom.command(cmd, this)
        response.nextRoom = response.nextRoomName.room
        return response
    }

    fun flag(name: String) = flags.get(name)

    fun inventoryHas(item: String): Boolean = inventory.contains(item)

    fun inventorySetInformation(item: String, property: String) {
        inventory.setInformation(item,property)
    }

    fun say(s: String) {
        response.say(s)
    }

    private fun showInventory() {
        say( inventory.asCarried() )
    }

}

