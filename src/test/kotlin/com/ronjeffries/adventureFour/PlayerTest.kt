package com.ronjeffries.adventureFour

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PlayerTest {
    @Test
    fun gameCheck() {
        val world = world {
            room("woods") {
                go("south","clearing")
                go("xyzzy","Y2")
            }
            room("clearing") {
                go("north", "woods")
            }
            room("Y2") {
                go("xyzzy","woods")
                go("south", "Y3")
            }
        }
        val player = Player(world, "woods")
        assertThat(player.currentRoomName).isEqualTo("woods")
        player.command("s")
        assertThat(player.currentRoomName).isEqualTo("clearing")
        player.command("n")
        assertThat(player.currentRoomName).isEqualTo("woods")
        player.command("xyzzy")
        assertThat(player.currentRoomName).isEqualTo("Y2")
        player.command("xyzzy")
        assertThat(player.currentRoomName).isEqualTo("woods")
        player.command("xyzzy")
        assertThat(player.currentRoomName).isEqualTo("Y2")
        player.command("s") // points to Y3 erroneously
        assertThat(player.currentRoomName).isEqualTo("Y2")
        // mp such room as Y3, defaults to stay in Y2
        player.command("xyzzy")
        assertThat(player.currentRoomName).isEqualTo("woods")
        val refs = player.roomReferences
        assertThat(refs).contains("Y3")
        val expected = setOf("Y3", "Y2", "clearing", "woods")
        assertThat(refs).isEqualTo(expected)
    }

    @Test
    fun roomDescriptions() {
        val long = "You're somewhere with a long descriptions"
        val world = world {
            room("somewhere") {
                desc("You're somewhere.", long)
            }
        }
        val room = world.roomNamedOrDefault("somewhere", Room("xxxx"))
        assertThat(room.longDesc).isEqualTo(long)
    }

    @Test
    fun `game gets good results`() {
        val world = world {
            room("first"){
                desc("short first", "long first")
                go("south","second")
            }
            room("second") {
                desc("short second", "long second")
                go("north", "first")
            }
        }
        val player = Player(world, "first")
        var resultString = player.command("s")
        assertThat(resultString).isEqualTo("long second\n")
        resultString = player.command("s")
        assertThat(resultString).isEqualTo("short second\n")
    }

    @Test
    fun `game can provide sayings`() {
        val myWorld = world {
            room("first") {
                desc("You're in the first room.", "You find yourself in the fascinating first room.")
                go("north", "second") { true }
                go("south","second") {
                    it.say("The grate is closed!")
                    false
                }
            }
            room("second") {
                desc("second room", "the long second room")
            }
        }
        val player = Player(myWorld, "first")
        val resultString = player.command("s")
        assertThat(resultString).isEqualTo("The grate is closed!\n" +
                "You find yourself in the fascinating first room.\n")
    }

    @Test
    fun `magic unlocks door`() {
        val world = world {
            room("palace") {
                desc("You are in an empty room.",
                    "You are in an empty room in the palace. "
                            + "There is a padlocked door to the east.")
                go("east","treasure") {
                    if (flag("unlocked").isTrue)
                        true
                    else {
                        say("The room is locked by a glowing lock!")
                        false
                    }
                }
            }
            room("treasure") {
                desc("You're in the treasure room",
                    "You are in a treasure room, rich with gold and jewels")
                go("west", "palace")
            }
        }
        val player = Player(world,"palace")
        var resultString = player.command("e")
        assertThat(resultString).isEqualTo("The room is locked by a glowing lock!\n" +
                "You are in an empty room in the palace. There is a padlocked door to the east.\n")
        resultString = player.command("wd40")
        assertThat(resultString).contains("unlocked")
        resultString = player.command("e")
        assertThat(resultString).contains("rich with gold")
    }
}