package com.ronjeffries.adventureFour

typealias GoTarget = Pair<R, (World)->Boolean>
typealias RoomLambda = Room.() -> Unit

enum class R {
    Spring, Wellhouse, Woods12, Woods9, Woods6, WoodsS,  WoodsNearCave, CaveEntrance,
    LowCave, Clearing, Y2,
    EastPit, WestPit, BottomPit,
    Z_FIRST, Z_SECOND, Z_PALACE, Z_TREASURE,
    ;

    var room = Room(this)
        private set

    fun freshRoom(): Room = Room(this).apply {room = this }
}

enum class D {
    North, South, East, West,
    Northwest, Southwest,Northeast,Southeast,
    Up, Down;

    companion object {
        fun executeIfDirectionExists(direction: String, function: (D) -> Unit) {
            valueMatching(direction)?.let { function(it) }
        }

        private fun valueMatching(direction: String): D? {
            return values().find {it.name.equals(direction, ignoreCase = true)}
        }
    }
}

fun world(details: World.()->Unit): World = World().apply { details() }

class World(val actions: IActions = Actions()) :IActions by actions {
    init {
        makeActions()
    }
    val lexicon = makeLexicon()
    val facts = Facts()
    val inventory: Items = Items()
    val name = "world"
    var response: GameResponse = GameResponse()


    // DSL

    fun room(name: R, details: RoomLambda): Room = name.freshRoom().apply {details()}

// Game Play

    fun addToInventory(item: Item) {
        inventory.add(item)
    }

    fun beam(imp: Imperative ) {
        val match = R.values().find { it.name.equals(imp.noun, ignoreCase = true)}
        match?.let { response.moveToRoomNamed(it)}
    }


    fun command(cmd: Command, currentRoom: Room): GameResponse {
        response = GameResponse(currentRoom.roomName)
        currentRoom.command(cmd, this)
        return response
    }

    fun inventoryHas(item: String): Boolean = inventory.contains(item)

    fun inventorySetInformation(item: String, property: String) {
        inventory.setInformation(item, property)
    }

    fun say(s: String) = response.say(s)

    fun yes(s:String): Boolean {
        say(s)
        return true
    }

    fun no(s:String): Boolean {
        say(s)
        return false
    }

    private fun showInventory() = say(inventory.asCarried())

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
            "up" to Phrase("go", "up"),
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
            add(Phrase("beam")) { imp -> imp.world.beam(imp)}
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

