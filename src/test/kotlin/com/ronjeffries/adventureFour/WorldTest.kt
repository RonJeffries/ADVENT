package com.ronjeffries.adventureFour

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class WorldTest {
    @Test
    fun hookAgain() {
        assertThat(2).isEqualTo(2)
    }

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
        assertThat("bar").isEqualTo("bar")
        assertThat(world.rooms.size).isEqualTo(1)
        assertThat(world.rooms[0].name).isEqualTo("living room")
    }
}

fun world(init: World.()->Unit): World{
    val world = World()
    world.init()
    return world
}