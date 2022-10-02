package com.ronjeffries.adventureFour

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ItemTest {
    @Test
    fun `initial item`() {
        var myItem = Item("nothing")
        world {
            room(R.ZTestFirst) {
                myItem = item("axe") {
                    desc("an axe", "an ornate axe belonging to the dwarf Bridget Ingridsdotter")
                }
            }
        }
        assertThat(myItem.shortDesc).isEqualTo("an axe")
        assertThat(myItem.longDesc).contains("Bridget Ingridsdotter")
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