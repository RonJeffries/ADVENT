package com.ronjeffries.adventureFour

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class WorldTest {
    @Test
    fun createWorld() {
        val world = world {println("creating")}
        assertThat(world.name).isEqualTo("world")
    }

    @Test
    fun worldWithRoom() {
        val world = world {
            room("living room") {}
        }
        assertThat(world.roomCount).isEqualTo(1)
        assertThat(world.hasRoomNamed("living room")).isEqualTo(true)
    }

    @Test
    internal fun roomsWithGo() {
        val world = world {
            room("woods") {
                go("s","clearing")
            }
            room("clearing") {
                go("n", "woods")
            }
        }
        assertThat(world.roomCount).isEqualTo(2)
        assert(world.hasRoomNamed("clearing"))
        val clearing:Room = world.unsafeRoomNamed("clearing")
        val newLocName:String = clearing.move("n", "", world)
        assertThat(newLocName).isEqualTo("woods")
    }

    @Test
    fun `world has inventory`() {
        val world = world {
            room("woods") {
                go("s","clearing")
            }
        }
        world.take("axe")
        world.take("bottle")
        assertThat(world.inventoryHas("axe"))
        val game = Game(world,"woods")
        game.command("inventory")
        val s: String = game.resultString
        assertThat(s).contains("axe")
        assertThat(s).contains("bottle")
        assertThat(s).isEqualTo("You have axe, bottle.\n\n\n")
    }
}
