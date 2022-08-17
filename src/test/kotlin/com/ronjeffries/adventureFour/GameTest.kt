package com.ronjeffries.adventureFour

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class GameTest {
    @Test
    internal fun gameCheck() {
        val world = world {
            room("woods") {
                go("s","clearing")
                go("xyzzy","Y2")
            }
            room("clearing") {
                go("n", "woods")
            }
            room("Y2") {
                go("xyzzy","woods")
                go("s", "Y3")
            }
        }
        val game = Game(world, "woods")
        assertThat(game.currentRoomName).isEqualTo("woods")
        game.command("s")
        assertThat(game.currentRoomName).isEqualTo("clearing")
        game.command("n")
        assertThat(game.currentRoomName).isEqualTo("woods")
        game.command("xyzzy")
        assertThat(game.currentRoomName).isEqualTo("Y2")
        game.command("xyzzy")
        assertThat(game.currentRoomName).isEqualTo("woods")
        game.command("xyzzy")
        assertThat(game.currentRoomName).isEqualTo("Y2")
        game.command("s") // points to Y3 erroneously
        assertThat(game.currentRoomName).isEqualTo("Y2")
        // mp such room as Y3, defaults to stay in Y2
        game.command("xyzzy")
        assertThat(game.currentRoomName).isEqualTo("woods")
        val refs = game.roomReferences
        assertThat(refs).contains("Y3")
        val expected = setOf("Y3", "Y2", "clearing", "woods")
        assertThat(refs).isEqualTo(expected)
    }
}