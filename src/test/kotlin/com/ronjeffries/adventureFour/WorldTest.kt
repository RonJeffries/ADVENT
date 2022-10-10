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
        val roomName = R.WoodsS
        val fakePlayer = Player(world, R.WoodsS)
        val response = world.command(Command("take axe"), roomName, fakePlayer)
        assertThat(response.resultString).contains("You are in the dark woods.\n")
        assertThat(response.resultString).doesNotContain("take axe taken")
        assertThat(response.resultString).contains("axe taken")
        assertThat(response.resultString).doesNotContain("You find axe.")
        val r2 =world.command(Command("take axe"), roomName, fakePlayer)
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
        val roomName = R.WoodsS
        val badCommand = Command("abcd efgh")
        val response = world.command(badCommand, roomName, Player(world,R.WoodsS))
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
        val roomName = R.Z_PALACE
        val command = Command("look around")
        val response = world.command(command, roomName, Player(world, R.Z_PALACE))
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
        val roomName = R.Z_PALACE
        val command = Command("look around")
        val fakePlayer = Player(world, R.Z_PALACE)
        var response = world.command(command,roomName, fakePlayer)
        assertThat(response.resultString).contains("locked door")
        val door = theFacts["door"]
        door.not()
        response = world.command(command,roomName, fakePlayer)
        assertThat(response.resultString).contains("door is open")
    }

}
