package com.ronjeffries.adventureFour

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RoomTest {
    @Test
    fun description() {
        val room = Room(R.Z_FIRST)
        room.desc("You are somewhere", "You are somewhere very mysterious with a kind of spooky feeling:")
        assertThat(room.shortDesc()).isEqualTo("You are somewhere")
    }

    @Test
    fun `room go has optional block`() {
        val myWorld = world {
            room(R.Z_FIRST) {
                desc("first room", "the long first room")
                go(D.North, R.Z_SECOND) { true }
                go(D.South,R.Z_SECOND) {
                    it.say("The grate is closed!")
                    false
                }
            }
            room(R.Z_SECOND){}
        }
        val myRoomName = R.Z_FIRST
        val secondRoomName = R.Z_SECOND
        val cmd = Command("s")
        val resp1 = myWorld.command(cmd, myRoomName)
        assertThat(resp1.nextRoomName).isEqualTo(R.Z_FIRST)
        assertThat(resp1.sayings).isEqualTo("The grate is closed!\n")
        val resp2 = myWorld.command(Command("s"), secondRoomName)
        assertThat(resp2.nextRoomName).isEqualTo(R.Z_SECOND)
    }

    @Test
    fun `room has contents`() {
        world {
            room(R.Z_FIRST) {
                desc("storage room", "large storage room")
                item("broom") {
                    desc("a broom")
                }
                item("broom") {
                    desc("a broom")
                }
                item("water") {
                    desc("some water")
                }
                item("axe") {
                    desc("an axe")
                }
            }
        }
        val room = R.Z_FIRST.room
        assertThat(room.contents.contains("broom")).isEqualTo(true)
        assertThat(room.contents.size).isEqualTo(3)
        val itemString = room.itemString()
        assertThat(itemString).contains("an axe")
        assertThat(itemString).contains("a broom")
        assertThat(itemString).contains("some water")
    }

    @Test
    fun `can take inventory`() {
        val world = world {
            room(R.Z_FIRST) {
                desc("storage room", "large storage room")
                item("broom") {
                    desc("a broom")
                }
            }
        }
        val player = Player(world, R.Z_FIRST)
        var resultString = player.command("take broom")
        assertThat(resultString).isEqualTo("broom taken.\nlarge storage room\n")
        resultString = player.command("inventory")
        assertThat(resultString).isEqualTo("You have a broom.\n\nstorage room\n")
    }

    @Test
    fun `taking water`() {
        val world = world {
            room(R.Spring) {
                desc("spring", "fresh water spring")
                item("bottle") {
                    desc("a bottle")
                }
                item("water") {
                    desc("water", "cool, fresh water")
                }
                action("take", "water") { imp->
                    if (inventoryHas("bottle")) {
                        inventorySetInformation("bottle", " of water")
                        say("You fill your bottle with water.")
                    } else {
                        imp.say("What would you keep it in?") }
                }
            }
        }
        val player = Player(world, R.Spring)
        var resultString = player.command("take water")
        assertThat(resultString).contains("What would you keep it in?")
        resultString = player.command("take bottle")
        assertThat(resultString).contains("bottle taken")
        assertThat(resultString).contains("You find water.")
        resultString = player.command("take water")
        assertThat(resultString).contains("You fill your bottle with water.")
        assertThat(resultString).contains("You find water")
        resultString = player.command("inventory")
        assertThat(resultString).contains("bottle of water")
    }

    @Test
    fun `actions with one and two words, in a list`() {
        val world = world {
            room(R.Z_FIRST) {
                val actions = listOf("unlock gate", "unlock", "open gate")
                action(actions) {
                    say("unlocking")
                }
            }
        }
        val player = Player(world, R.Z_FIRST)
        var result = player.command("unlock gate")
        assertThat(result).contains("unlocking")
        result = player.command("unlock")
        assertThat(result).contains("unlocking")
        result = player.command("open gate")
        assertThat(result).contains("unlocking")
    }

    @Test
    fun `world actions with one and two words, in a list`() {
        val world = world {
            val actions = listOf("wave wand", "lux", "say aperto")
            action(actions) {
                say("no magic allowed here")
            }
            room(R.Z_FIRST) {
            }
        }
        val player = Player(world, R.Z_FIRST)
        var result = player.command("wave wand")
        assertThat(result).contains("no magic allowed here")
        result = player.command("lux")
        assertThat(result).contains("no magic allowed here")
        result = player.command("say aperto")
        assertThat(result).contains("no magic allowed here")
    }

    @Test
    fun `can beam between rooms`() {
        val world = world {
            room(R.Z_FIRST) {
                desc("first", "first")
            }
            room(R.Z_SECOND) {
                desc("second", "second")
            }
        }
        val player = Player(world, R.Z_FIRST)
        val result = player.command("beam Z_SECOND")
        assertThat(result).contains("second")
        val r2 = player.command("beam Z_NO_SUCH")
        assertThat(r2).contains("No such place as z_no_such.")
    }

    @Test
    fun `darkness room`() {
        val world = world {
            room(R.Z_FIRST) {
                desc("You are in nondescript room.",
                    "You are in a nondescript room. A darkened hall leads south.")
                go(D.South, R.Darkness)
            }
            room(R.Darkness) {
                desc("Darkness", "Darkness. You are likely to be eaten by a grue.")
                go(D.North, R.Z_FIRST)
                action {
                    if (it.verb=="go" && it.noun=="north") {
                        it.notHandled()
                    } else {
                        say("You have been eaten by a grue!")
                    }
                }
            }
        }
        val player = Player(world, R.Z_FIRST)
        var result = player.command("s")
        assertThat(result).contains("grue")
        result = player.command("n")
        assertThat(result).contains("nondescript")
        player.command("s") // get back mama. maybe do this in other order?
        result = player.command("e")
        assertThat(result).contains("You have been eaten")
    }

    @Test
    fun `lamp on returns player to prior room`() {
        val world = world {
            room(R.Z_FIRST) {
                desc("You are in well-lighted room.",
                    "You are in a well-lighted room. A darkened hall leads south.")
                go(D.South, R.Darkness)
            }
            room(R.Darkness) {
                desc("Darkness", "Darkness. You are likely to be eaten by a grue.")
                action {
                    if (it.verb == "lamp" && it.noun=="on") {
//                        response.goToPriorRoom()
                    } else if (it.verb=="do" && it.noun == "something"){
                        // ignore command
                    }
                }
            }
        }
        val player = Player(world, R.Z_FIRST)
        var result = player.command("s")
        assertThat(result).contains("Darkness")
//        player.command("do something")
//        result = player.command("lamp on")
//        assertThat(result).contains("well-lighted")
    }
}