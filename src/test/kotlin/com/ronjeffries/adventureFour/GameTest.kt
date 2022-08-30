package com.ronjeffries.adventureFour

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class GameTest {
    @Test
    fun gameCheck() {
        val world = world {
            room("woods") {
                go("s","clearing")
                go("xyzzy","Y2")
            }
            room("clearing") {
                go("n", "woods")
            }
            room("Y2") {
                go("xyzzy","woods")
                go("s", "Y3")
            }
        }
        val game = Game(world, "woods")
        assertThat(game.currentRoomName).isEqualTo("woods")
        game.command("s")
        assertThat(game.currentRoomName).isEqualTo("clearing")
        game.command("n")
        assertThat(game.currentRoomName).isEqualTo("woods")
        game.command("xyzzy")
        assertThat(game.currentRoomName).isEqualTo("Y2")
        game.command("xyzzy")
        assertThat(game.currentRoomName).isEqualTo("woods")
        game.command("xyzzy")
        assertThat(game.currentRoomName).isEqualTo("Y2")
        game.command("s") // points to Y3 erroneously
        assertThat(game.currentRoomName).isEqualTo("Y2")
        // mp such room as Y3, defaults to stay in Y2
        game.command("xyzzy")
        assertThat(game.currentRoomName).isEqualTo("woods")
        val refs = game.roomReferences
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
                go("s","second")
            }
            room("second") {
                desc("short second", "long second")
                go("n", "first")
            }
        }
        val game = Game(world, "first")
        game.command("s")
        assertThat(game.resultString).isEqualTo("long second\n")
        game.command("s")
        assertThat(game.resultString).isEqualTo("long second\n")
    }

    @Test
    fun `game can provide sayings`() {
        val myWorld = world {
            room("first") {
                desc("You're in the first room.", "You find yourself in the fascinating first room.")
                go("n", "second") { true }
                go("s","second") {
                    it.say("The grate is closed!")
                    false
                }
            }
            room("second") {
                desc("second room", "the long second room")
            }
        }
        val game = Game(myWorld, "first")
        game.command("s")
        assertThat(game.resultString).isEqualTo("The grate is closed!\n" +
                "You find yourself in the fascinating first room.\n")
    }

    @Test
    fun `magic unlocks door`() {
        val world = world {
            room("palace") {
                desc("You are in an empty room.",
                    "You are in an empty room in the palace. "
                            + "There is a padlocked door to the east.")
                go("e","treasure") {
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
                go("w", "palace")
            }
        }
        val game = Game(world,"palace")
        game.command("e")
        assertThat(game.resultString).isEqualTo("The room is locked by a glowing lock!\n" +
                "You are in an empty room in the palace. There is a padlocked door to the east.\n")
        game.command("wd40")
        assertThat(game.resultString).contains("unlocked")
        game.command("e")
        assertThat(game.resultString).contains("rich with gold")
    }
}