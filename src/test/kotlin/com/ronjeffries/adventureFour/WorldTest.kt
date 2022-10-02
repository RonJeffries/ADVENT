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
    fun roomsWithGo() {
        val world = world {
            room(R.Woods) {
                go(D.South,R.Clearing)
            }
            room(R.Clearing) {
                go(D.North, R.Woods)
            }
        }
        val player = Player(world, R.Clearing)
        player.command("go n")
        assertThat(world.response.nextRoomName).isEqualTo(R.Woods)
    }

    @Test
    fun `go blue`() {
        val world = world {
            room(R.Woods) {
                go(D.South,R.Clearing)
            }
        }
        val player = Player(world, R.Woods)
        player.command("go blue")
        assertThat(world.response.nextRoomName).isEqualTo(R.Woods)
    }

    @Test
    fun `world has inventory`() {
        val world = world {
            room(R.Woods) {
                go(D.South,R.Clearing)
            }
        }
        world.addToInventory(Item("axe"))
        world.addToInventory(Item("bottle"))
        assertThat(world.inventoryHas("axe"))
        val player = Player(world,R.Woods)
        val resultString = player.command("inventory")
        assertThat(resultString).contains("axe")
        assertThat(resultString).contains("bottle")
        assertThat(resultString).isEqualTo("You have axe, bottle.\n\n\n")
    }

    @Test
    fun `take command works`() {
        val world = world {
            room(R.Woods) {
                desc("You are in the woods.", "You are in the dark woods.")
                item("axe") {}
            }
        }
        val room = world.unsafeRoomNamed(R.Woods)
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
        assertThat(lex.synonym("e")).isEqualTo("east")
        val imp = lex.translate("e")
        assertThat(imp.verb).isEqualTo("go")
        assertThat(imp.noun).isEqualTo("east")
    }

    @Test
    fun `unknown command`() {
        val world = world {
            room(R.Woods) {
                desc("You are in the woods.", "You are in the dark woods.")
            }
        }
        val room = world.unsafeRoomNamed(R.Woods)
        val badCommand = Command("abcd efgh")
        val response = world.command(badCommand, room)
        assertThat(response.resultString).contains("I do not understand 'abcd efgh'.")
    }

    @Test
    fun `room action`() {
        val world = world {
            room(R.Palace) {
                desc("you are in a busy room", "you are in a very busy room")
                action("look","around",
                    {imp-> imp.world.say("Lots happening")})
            }
        }
        val room = world.unsafeRoomNamed(R.Palace)
        val command = Command("look around")
        val response = world.command(command, room)
        assertThat(response.resultString).contains("Lots happening")
    }
}
