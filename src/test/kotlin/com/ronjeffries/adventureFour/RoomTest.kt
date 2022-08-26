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
        }
        val myRoom = myWorld.unsafeRoomNamed("first")
        val response = GameResponse()
        myWorld.response = response
        myRoom.command("s", myWorld)
        assertThat(response.nextRoomName).isEqualTo("first")
        assertThat(response.sayings).isEqualTo("The grate is closed!\n")
        val r2 = GameResponse()
        myWorld.response = r2
        myRoom.command("n", myWorld)
        assertThat(r2.nextRoomName).isEqualTo("second")
    }

    @Test
    fun `room has contents`() {
        val world = world {
            room("storage") {
                desc("storage room", "large storage room")
                item("broom")
                item("broom")
            }
        }
        val room = world.unsafeRoomNamed("storage")
        assertThat(room.contents).contains("broom")
        assertThat(room.contents.size).isEqualTo(1)
    }
}