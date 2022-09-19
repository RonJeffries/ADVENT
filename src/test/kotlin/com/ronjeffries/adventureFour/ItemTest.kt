package com.ronjeffries.adventureFour

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ItemTest {
    @Test
    fun `initial item`() {
        var myRoom = Room("y")
        world {
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

    @Test
    fun `moving items`() {
        val axe = Item("axe")
        val inventory = Items()
        val roomContents = Items()
        roomContents.add(axe)
        assertThat(inventory.contains("axe")).isEqualTo(false)
        var done:Boolean = roomContents.moveItemTo("axe", inventory)
        assertThat(inventory.contains("axe")).isEqualTo(true)
        assertThat(done).isEqualTo(true)
        done = roomContents.moveItemTo("axe", inventory)
        assertThat(done).isEqualTo(false)
    }
}