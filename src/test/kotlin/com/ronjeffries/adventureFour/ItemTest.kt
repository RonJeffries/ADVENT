package com.ronjeffries.adventureFour

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ItemTest {
    @Test
    fun `initial item`() {
        var myRoom = Room("y")
        val world = world {
            myRoom = room("x") {
                item("axe")
            }
        }
        val item: Item = myRoom.contents["axe"]!!
        assertThat(item.name).isEqualTo("axe")
    }
}