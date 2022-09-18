package com.ronjeffries.adventureFour

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ItemTest {
    @Test
    fun `initial item`() {
        var myRoom = Room("y")
        val world = world {
            myRoom = room("x") {
                item("axe") {
                    desc("an axe", "an ornate axe belonging to the dwarf Bridget Ingridsdotter")
                }
            }
        }
        val item: Item = myRoom.contents.getItem("axe")!!
        assertThat(item.shortDesc).isEqualTo("an axe")
        assertThat(item.longDesc).contains("Bridget Ingridsdotter")
    }
}