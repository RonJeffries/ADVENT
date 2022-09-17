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
    fun roomsWithGo() {
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
        world.addToInventory(Item("axe"))
        world.addToInventory(Item("bottle"))
        assertThat(world.inventoryHas("axe"))
        val player = Player(world,"woods")
        val resultString = player.command("inventory")
        assertThat(resultString).contains("axe")
        assertThat(resultString).contains("bottle")
        assertThat(resultString).isEqualTo("You have axe, bottle.\n\n\n")
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
        val response = world.command(Command("take axe"), room)
        assertThat(response.resultString).contains("You are in the dark woods.\n")
        assertThat(response.resultString).doesNotContain("take axe taken")
        assertThat(response.resultString).contains("axe taken")
        assertThat(response.resultString).doesNotContain("You find axe.")
        val r2 =world.command(Command("take axe"), room)
        assertThat(r2.resultString).contains("I see no axe here!\n")
    }

    @Test
    fun `world has lexicon`() {
        val world = world {}
        val lex = world.lexicon
        assertThat(lex.synonym("east")).isEqualTo("e")
        val imp = lex.translate("e")
        assertThat(imp.verb).isEqualTo("go")
        assertThat(imp.noun).isEqualTo("e")
    }

    @Test
    fun `unknown command`() {
        val world = world {
            room("woods") {
                desc("You are in the woods.", "You are in the dark woods.")
            }
        }
        val room = world.unsafeRoomNamed("woods")
        val badCommand = Command("abcd efgh")
        val response = world.command(badCommand, room)
        assertThat(response.resultString).contains("unknown command 'abcd efgh'")
    }

    @Test
    fun `room action`() {
        val world = world {
            room("busy") {
                desc("you are in a busy room", "you are in a very busy room")
                action(Phrase("look","around"),
                    {imp-> imp.world.say("Lots happening")})
            }
        }
        val room = world.unsafeRoomNamed("busy")
        val command = Command("look around")
        val response = world.command(command, room)
        assertThat(response.resultString).contains("Lots happening")
    }
}
