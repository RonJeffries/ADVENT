package com.ronjeffries.adventureFour

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PlayerTest {
    @Test
    fun gameCheck() {
        val world = world {
            room(R.Woods) {
                go(D.South, R.Clearing)
                go(D.West, R.Y2)
            }
            room(R.Clearing) {
                go(D.North,R.Woods)
            }
            room(R.Y2) {
                go(D.West,R.Woods)
                go(D.South,R.Y2)
            }
        }
        val player = Player(world, R.Woods)
        assertThat(player.currentRoomName).isEqualTo(R.Woods)
        player.command("s")
        assertThat(player.currentRoomName).isEqualTo(R.Clearing)
        player.command("n")
        assertThat(player.currentRoomName).isEqualTo(R.Woods)
        player.command("west")
        assertThat(player.currentRoomName).isEqualTo(R.Y2)
        player.command("west")
        assertThat(player.currentRoomName).isEqualTo(R.Woods)
        player.command("west")
        assertThat(player.currentRoomName).isEqualTo(R.Y2)
        // cannot happen with new go command
//        player.command("s") // points to Y3 erroneously
//        assertThat(player.currentRoomName).isEqualTo("Y2")
        // no such room as Y3, defaults to stay in Y2
        player.command("west")
        assertThat(player.currentRoomName).isEqualTo(R.Woods)
        val refs = player.roomReferences
        // refs no longer valid with R enum in play
//        assertThat(refs).contains("Y3")
        val expected = setOf( R.Y2, R.Clearing, R.Woods)
        assertThat(refs).isEqualTo(expected)
    }

    @Test
    fun roomDescriptions() {
        val long = "You're somewhere with a long descriptions"
        world {
            room(R.First) {
                desc("You're somewhere.", long)
            }
        }
        val room = R.First.room
        assertThat(room.longDesc).isEqualTo(long)
    }

    @Test
    fun `game gets good results`() {
        val world = world {
            room(R.First){
                desc("short first", "long first")
                go(D.South,R.Second)
            }
            room(R.Second) {
                desc("short second", "long second")
                go(D.North,R.First)
            }
        }
        val player = Player(world, R.First)
        var resultString = player.command("s")
        assertThat(resultString).isEqualTo("long second\n")
        resultString = player.command("s")
        assertThat(resultString).isEqualTo("short second\n")
    }

    @Test
    fun `game can provide sayings`() {
        val myWorld = world {
            room(R.First) {
                desc("You're in the first room.", "You find yourself in the fascinating first room.")
                go(D.North,R.Second) { true }
                go(D.South,R.Second) {
                    say("The grate is closed!")
                    false
                }
            }
            room(R.Second) {
                desc("second room", "the long second room")
            }
        }
        val player = Player(myWorld, R.First)
        val resultString = player.command("s")
        assertThat(resultString).isEqualTo("The grate is closed!\n" +
                "You find yourself in the fascinating first room.\n")
    }

    @Test
    fun `magic unlocks door`() {
        val world = world {
            room(R.Palace) {
                desc("You are in an empty room.",
                    "You are in an empty room in the palace. "
                            + "There is a padlocked door to the east.")
                action("say", "wd40") {
                    flags.get("unlocked").set(true)
                    say("The padlock is unlocked!")
                }
                go(D.East,R.Treasure) {
                    if (flag("unlocked").isTrue)
                        true
                    else {
                        say("The room is locked by a glowing lock!")
                        false
                    }
                }
            }
            room(R.Treasure) {
                desc("You're in the treasure room",
                    "You are in a treasure room, rich with gold and jewels")
                go(D.West,R.Palace)
            }
        }
        val player = Player(world,R.Palace)
        var resultString = player.command("e")
        assertThat(resultString).isEqualTo("The room is locked by a glowing lock!\n" +
                "You are in an empty room in the palace. There is a padlocked door to the east.\n")
        resultString = player.command("wd40")
        assertThat(resultString).contains("unlocked")
        resultString = player.command("e")
        assertThat(resultString).contains("rich with gold")
    }
}