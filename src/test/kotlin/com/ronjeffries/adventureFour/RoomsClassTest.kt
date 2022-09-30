package com.ronjeffries.adventureFour

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RoomsClassTest {

    @Test
    fun roomsCanAddAndGet() {
        val rooms = Rooms()
        val room = Room(R.First)
        rooms.add(room)
        val found = rooms.getOrDefault(R.First, Room(R.Second))
        assertThat(found).isEqualTo(room)
    }
}