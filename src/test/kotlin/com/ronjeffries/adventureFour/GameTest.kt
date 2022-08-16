package com.ronjeffries.adventureFour

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class GameTest {
    @Test
    internal fun gameCheck() {
        val world = world {
            room("woods") {
                go("s","clearing")
            }
            room("clearing") {
                go("n", "woods")
            }
        }
        val game = Game(world, "woods")
        assertThat(game.currentRoomName).isEqualTo("woods")
        game.command("s")
        assertThat(game.currentRoomName).isEqualTo("clearing")
        game.command("n")
        assertThat(game.currentRoomName).isEqualTo("woods")
    }
}