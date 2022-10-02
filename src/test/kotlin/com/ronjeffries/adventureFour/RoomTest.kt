package com.ronjeffries.adventureFour

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RoomTest {
    @Test
    fun description() {
        val room = Room(R.ZTestFirst)
        room.desc("You are somewhere", "You are somewhere very mysterious with a kind of spooky feeling:")
        assertThat(room.shortDesc).isEqualTo("You are somewhere")
    }

    @Test
    fun `room go has optional block`() {
        val myWorld = world {
            room(R.ZTestFirst) {
                desc("first room", "the long first room")
                go(D.North, R.ZTestSecond) { true }
                go(D.South,R.ZTestSecond) {
                    it.say("The grate is closed!")
                    false
                }
            }
            room(R.ZTestSecond){}
        }
        val myRoom = R.ZTestFirst.room
        val secondRoom = R.ZTestSecond.room
        val cmd = Command("s")
        val resp1 = myWorld.command(cmd, myRoom)
        assertThat(resp1.nextRoomName).isEqualTo(R.ZTestFirst)
        assertThat(resp1.sayings).isEqualTo("The grate is closed!\n")
        val resp2 = myWorld.command(Command("s"), secondRoom)
        assertThat(resp2.nextRoomName).isEqualTo(R.ZTestSecond)
    }

    @Test
    fun `room has contents`() {
        world {
            room(R.ZTestFirst) {
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
        val room = R.ZTestFirst.room
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
            room(R.ZTestFirst) {
                desc("storage room", "large storage room")
                item("broom") {
                    desc("a broom")
                }
            }
        }
        val player = Player(world, R.ZTestFirst)
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
}