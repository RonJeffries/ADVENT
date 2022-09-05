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
        val player = Player(world, "clearing")
        player.command("go n")
        assertThat(world.response.nextRoomName).isEqualTo("woods")
    }

    @Test
    fun `world has inventory`() {
        val world = world {
            room("woods") {
                go("s","clearing")
            }
        }
        world.addToInventory("axe")
        world.addToInventory("bottle")
        assertThat(world.inventoryHas("axe"))
        val player = Player(world,"woods")
        player.command("inventory")
        val s: String = player.resultString
        assertThat(s).contains("axe")
        assertThat(s).contains("bottle")
        assertThat(s).isEqualTo("You have axe, bottle.\n\n\n")
    }

    @Test
    fun `take command works`() {
        val world = world {
            room("woods") {
                desc("You are in the woods.", "You are in the dark woods.")
                item("axe")
            }
        }
        val room = world.unsafeRoomNamed("woods")
        val response = GameResponse()
        response.nextRoom = room // hackery
        world.response = response
        room.command(Command("take axe"), world)
        assertThat(world.resultString).contains("You are in the dark woods.\n")
        assertThat(world.resultString).doesNotContain("take axe taken")
        assertThat(world.resultString).contains("axe taken")
        assertThat(world.resultString).doesNotContain("You find axe.")
        val r2 = GameResponse()
        world.response = r2
        r2.nextRoom = room
        room.command(Command("take axe"), world)
        assertThat(r2.resultString).contains("I see no axe here!\n")
    }

    @Test
    fun `world has lexicon`() {
        val world = world {}
        val lex = world.lexicon
        assertThat(lex.synonym("e")).isEqualTo("east")
        val imp = lex.translate("east")
        assertThat(imp.verb).isEqualTo("go")
        assertThat(imp.noun).isEqualTo("east")
    }
}
