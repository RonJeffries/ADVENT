package com.ronjeffries.adventureFour

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RoomsClassTest {

    @Test
    fun roomsCanAddAndGet() {
        val rooms = Rooms()
        val room = Room("abc")
        rooms.add(room)
        val found = rooms.getOrDefault("abc", Room("zzz"))
        assertThat(found).isEqualTo(room)
    }
}