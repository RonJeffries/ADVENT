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
            room(R.WoodsS) {
                go(D.South,R.Clearing)
                go(D.Southwest, R.Clearing)
            }
            room(R.Clearing) {
                go(D.North, R.WoodsS)
            }
        }
        val player = Player(world, R.Clearing)
        player.command("go n")
        assertThat(world.response.nextRoomName).isEqualTo(R.WoodsS)
        player.command("go sw")
        assertThat(world.response.nextRoomName).isEqualTo(R.Clearing)
    }

    @Test
    fun `go blue`() {
        val world = world {
            room(R.WoodsS) {
                go(D.South,R.Clearing)
            }
        }
        val player = Player(world, R.WoodsS)
        player.command("go blue")
        assertThat(world.response.nextRoomName).isEqualTo(R.WoodsS)
    }

    @Test
    fun `world has inventory`() {
        val world = world {
            room(R.WoodsS) {
                go(D.South,R.Clearing)
            }
        }
        world.addToInventory(Item("axe"))
        world.addToInventory(Item("bottle"))
        assertThat(world.inventoryHas("axe"))
        val player = Player(world,R.WoodsS)
        val resultString = player.command("inventory")
        assertThat(resultString).contains("axe")
        assertThat(resultString).contains("bottle")
        assertThat(resultString).isEqualTo("You have axe, bottle.\n\n\n")
    }

    @Test
    fun `take command works`() {
        val world = world {
            room(R.WoodsS) {
                desc("You are in the woods.", "You are in the dark woods.")
                item("axe") {}
            }
        }
        val room = R.WoodsS.room
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
        val imp = lex.translate("east")
        assertThat(imp.verb).isEqualTo("go")
        assertThat(imp.noun).isEqualTo("east")
    }

    @Test
    fun `unknown command`() {
        val world = world {
            room(R.WoodsS) {
                desc("You are in the woods.", "You are in the dark woods.")
            }
        }
        val room = R.WoodsS.room
        val badCommand = Command("abcd efgh")
        val response = world.command(badCommand, room)
        assertThat(response.resultString).contains("I do not understand 'abcd efgh'.")
    }

    @Test
    fun `room action`() {
        val world = world {
            room(R.Z_PALACE) {
                desc("you are in a busy room", "you are in a very busy room")
                action("look","around",
                    {imp-> imp.world.say("Lots happening")})
            }
        }
        val room = R.Z_PALACE.room
        val command = Command("look around")
        val response = world.command(command, room)
        assertThat(response.resultString).contains("Lots happening")
    }

    @Test
    fun `variable description`() {
        var theFacts = Facts()
        val world = world {
            theFacts = facts
            val door = facts["door"]
            room(R.Z_PALACE) {
                fun doorInfo() = when(door.isTrue) {
                    true-> "The treasure room door is open to the west."
                    false -> "There is a locked door to the west."
                }
                desc(
                    { "You're in the Palace. " + doorInfo() },
                    { "You are in a Grand Palace. " + doorInfo() })
            }
        }
        val room = R.Z_PALACE.room
        val command = Command("look around")
        var response = world.command(command,room)
        assertThat(response.resultString).contains("locked door")
        val door = theFacts["door"]
        door.not()
        response = world.command(command,room)
        assertThat(response.resultString).contains("door is open")
    }

}
