package com.ronjeffries.adventureFour

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RoomTest {
    @Test
    fun description() {
        val room = Room("somewhere")
        room.desc("You are somewhere", "You are somewhere very mysterious with a kind of spooky feeling:")
        assertThat(room.shortDesc).isEqualTo("You are somewhere")
    }

    @Test
    fun `room go has optional block`() {
        val myWorld = world {
            room("first") {
                desc("first room", "the long first room")
                go("n", "second") { true }
                go("s","second") {
                    it.say("The grate is closed!")
                    false
                }
            }
            room("second"){}
        }
        val myRoom = myWorld.unsafeRoomNamed("first")
        val secondRoom = myWorld.unsafeRoomNamed("second")
        val cmd = Command("s")
        val resp1 = myWorld.command(cmd, myRoom)
        assertThat(resp1.nextRoomName).isEqualTo("first")
        assertThat(resp1.sayings).isEqualTo("The grate is closed!\n")
        val resp2 = myWorld.command(Command("s"), secondRoom)
        assertThat(resp2.nextRoomName).isEqualTo("second")
    }

    @Test
    fun `room has contents`() {
        val world = world {
            room("storage") {
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
        val room = world.unsafeRoomNamed("storage")
        assertThat(room.contents).containsKey("broom")
        assertThat(room.contents.size).isEqualTo(3)
        val itemString = room.itemString()
        assertThat(itemString).contains("an axe")
        assertThat(itemString).contains("a broom")
        assertThat(itemString).contains("some water")
    }

    @Test
    fun `can take inventory`() {
        val world = world {
            room("storage") {
                desc("storage room", "large storage room")
                item("broom") {
                    desc("a broom")
                }
            }
        }
        val player = Player(world, "storage")
        var resultString = player.command("take broom")
        assertThat(resultString).isEqualTo("broom taken.\nlarge storage room\n")
        resultString = player.command("inventory")
        assertThat(resultString).isEqualTo("You have a broom.\n\nlarge storage room\n")
    }
}