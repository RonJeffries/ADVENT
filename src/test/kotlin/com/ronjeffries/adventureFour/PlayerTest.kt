package com.ronjeffries.adventureFour

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PlayerTest {
    @Test
    fun gameCheck() {
        val world = world {
            room(R.WoodsS) {
                go(D.South, R.Clearing)
                go(D.West, R.Y2)
            }
            room(R.Clearing) {
                go(D.North,R.WoodsS)
            }
            room(R.Y2) {
                go(D.West,R.WoodsS)
                go(D.South,R.Y2)
            }
        }
        val player = Player(world, R.WoodsS)
        assertThat(player.currentRoomName).isEqualTo(R.WoodsS)
        player.command("s")
        assertThat(player.currentRoomName).isEqualTo(R.Clearing)
        player.command("n")
        assertThat(player.currentRoomName).isEqualTo(R.WoodsS)
        player.command("west")
        assertThat(player.currentRoomName).isEqualTo(R.Y2)
        player.command("west")
        assertThat(player.currentRoomName).isEqualTo(R.WoodsS)
        player.command("west")
        assertThat(player.currentRoomName).isEqualTo(R.Y2)
        // cannot happen with new go command
//        player.command("s") // points to Y3 erroneously
//        assertThat(player.currentRoomName).isEqualTo("Y2")
        // no such room as Y3, defaults to stay in Y2
        player.command("west")
        assertThat(player.currentRoomName).isEqualTo(R.WoodsS)
//        val refs = player.roomReferences
        // refs no longer valid with R enum in play
//        assertThat(refs).contains("Y3")
//        val expected = setOf( R.Y2, R.Clearing, R.Woods)
//        assertThat(refs).isEqualTo(expected)
    }

    @Test
    fun roomDescriptions() {
        val long = "You're somewhere with a long descriptions"
        world {
            room(R.Z_FIRST) {
                desc("You're somewhere.", long)
            }
        }
        val room = R.Z_FIRST.room
        assertThat(room.longDesc).isEqualTo(long)
    }

    @Test
    fun `game gets good results`() {
        val world = world {
            room(R.Z_FIRST){
                desc("short first", "long first")
                go(D.South,R.Z_SECOND)
            }
            room(R.Z_SECOND) {
                desc("short second", "long second")
                go(D.North,R.Z_FIRST)
            }
        }
        val player = Player(world, R.Z_FIRST)
        var resultString = player.command("s")
        assertThat(resultString).isEqualTo("long second\n")
        resultString = player.command("s")
        assertThat(resultString).isEqualTo("short second\n")
    }

    @Test
    fun `game can provide sayings`() {
        val myWorld = world {
            room(R.Z_FIRST) {
                desc("You're in the first room.", "You find yourself in the fascinating first room.")
                go(D.North,R.Z_SECOND) { true }
                go(D.South,R.Z_SECOND) {
                    say("The grate is closed!")
                    false
                }
            }
            room(R.Z_SECOND) {
                desc("second room", "the long second room")
            }
        }
        val player = Player(myWorld, R.Z_FIRST)
        val resultString = player.command("s")
        assertThat(resultString).isEqualTo("The grate is closed!\n" +
                "You find yourself in the fascinating first room.\n")
    }

    @Test
    fun `magic unlocks door`() {
        val world = world {
            room(R.Z_PALACE) {
                desc("You are in an empty room.",
                    "You are in an empty room in the palace. "
                            + "There is a padlocked door to the east.")
                action("say", "wd40") {
                    gameVariables["unlocked"].set(true)
                    say("The padlock is unlocked!")
                }
                go(D.East,R.Z_TREASURE) {
                    if (gameVariables["unlocked"].isTrue)
                        true
                    else {
                        say("The room is locked by a glowing lock!")
                        false
                    }
                }
            }
            room(R.Z_TREASURE) {
                desc("You're in the treasure room",
                    "You are in a treasure room, rich with gold and jewels")
                go(D.West,R.Z_PALACE)
            }
        }
        val player = Player(world,R.Z_PALACE)
        var resultString = player.command("e")
        assertThat(resultString).isEqualTo("The room is locked by a glowing lock!\n" +
                "You are in an empty room in the palace. There is a padlocked door to the east.\n")
        resultString = player.command("wd40")
        assertThat(resultString).contains("unlocked")
        resultString = player.command("e")
        assertThat(resultString).contains("rich with gold")
    }

    @Test
    fun `world can have special action`() {
        val world = world {
            action("exhibit","curiosity") { _: Imperative -> say("remember the cat")}
            room(R.Z_FIRST){
                desc("short first", "long first")
            }
        }
        val player = Player(world, R.Z_FIRST)
        val resultString = player.command("exhibit curiosity")
        assertThat(resultString).contains("remember the cat")
    }
}