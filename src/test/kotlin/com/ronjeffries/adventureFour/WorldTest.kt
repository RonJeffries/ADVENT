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
        assertThat(world.rooms[0].name).isEqualTo("living room")
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
        assertThat(world.rooms[1].name).isEqualTo("clearing")
        val r1Moves = world.rooms[1].moves[0]
        val expected = Pair("n","woods")
        assertThat(r1Moves).isEqualTo(expected)
    }
}

fun world(init: World.()->Unit): World{
    val world = World()
    world.init()
    return world
}