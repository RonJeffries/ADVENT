package com.ronjeffries.adventureFour

import javafx.scene.Parent
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.Priority
import tornadofx.*

var myText: TextArea by singleAssign()
var myCommand: TextField by singleAssign()
var textContents = "Welcome to Tiny Adventure!"

fun makeGameWorld(): World {
    val theWorld = world {
        room(R.Spring) {
            desc(
                "You are at the spring.", "You are at a clear water spring. " +
                        "There is a well house to the east, and a wooded area to the west and south."
            )
            item("water") {
                desc(
                    "water", "Perfectly ordinary sparkling spring water, " +
                            "with a tinge or iron that has leached from the surrounding rocks, " +
                            "and a minor tang of fluoride, which is good for your teeth.)"
                )
            }
            go(D.East, R.Wellhouse)
            go(D.West, R.Woods6)
            go(D.South, R.WoodsS)
            go(D.Northwest, R.Woods12)
            action("take", "water") { imp ->
                if (inventoryHas("bottle")) {
                    inventorySetInformation("bottle", " of water")
                    say("You fill your bottle with water.")
                } else {
                    imp.say("What would you keep it in?")
                }
            }
        }
        room(R.Wellhouse) {
            desc(
                "You are in the well house.", "You are in a small house near a spring. " +
                        "The house is sparsely decorated, in a rustic style. " +
                        "It appears to be well kept."
            )
            item("keys")
            item("matches")
            item("magazine")
            go(D.West, R.Spring)
            action("say", "xyzzy") {
                say("Swoosh!")
                response.moveToRoomNamed(R.WoodsNearCave)
            }
        }
        room(R.Woods6) {
            desc(
                "You are lost in the woods.", "You are in a dark and forbidding wooded area. " +
                        "It's not clear which way to go."
            )
            go(D.Northeast, R.Spring)
            go(D.West, R.Woods9)
        }
        room(R.Woods9) {
            desc(
                "You are lost in the woods.", "You are in a dark and forbidding wooded area. " +
                        "It's not clear which way to go."
            )
            go(D.Southeast, R.Woods6)
            go(D.North, R.Woods12)
        }
        room(R.Woods12) {
            desc(
                "You are lost in the woods.", "You are in a dark and forbidding wooded area. " +
                        "It's not clear which way to go."
            )
            go(D.West, R.Woods9)
            go(D.South, R.Spring)
            item("bottle")
        }
        room(R.WoodsS) {
            desc(
                "You are lost in the woods.", "You are in a dark and forbidding wooded area. " +
                        "It's not clear which way to go."
            )
            go(D.South, R.WoodsNearCave)
            go(D.Northwest, R.Woods6)
        }
        room(R.WoodsNearCave) {
            desc(
                "You are in the breezy woods.", "You are in the woods. " +
                        "There is a cool breeze coming from the west."
            )
            go(D.West, R.CaveEntrance)
            go(D.North, R.WoodsS)
        }
        room(R.CaveEntrance) {
            val gateIsUnlocked = facts["gateIsUnlocked"]
            fun gateMessage() = when (gateIsUnlocked.isTrue) {
                true -> "The gate is unlocked."
                false -> "The gate is locked."
            }
            desc(
                { "You are at the cave entrance. " + gateMessage() },
                {
                    "To the west, there is a gated entrance to a cave. " +
                            "A cool breeze emanates from the cave. " + gateMessage()
                }
            )
            go(D.East, R.WoodsNearCave)
            action("unlock", "gate") {
                if (inventoryHas("keys")) {
                    say("You fumble through the keys and finally unlock the gate!")
                    gateIsUnlocked.set(true)
                } else {
                    say("What is your clever plan for unlocking it")
                }
            }
            action("lock", "gate") {
                if (inventoryHas("keys")) {
                    say("Familiar as you are with the keys, you quickly lock the gate.")
                    gateIsUnlocked.set(false)
                } else {
                    say("Um, it's about the keys ... you don't have them.")
                }
            }
            go(D.West, R.LowCave) {
                when (gateIsUnlocked.truth) {
                    true -> yes("You swing open the unlocked gate and enter.")
                    false -> no("The gate is locked. I believe that I mentioned that earlier.")
                }
            }
        }
        room(R.LowCave) {
            val gateIsUnlocked = facts["gateIsUnlocked"]
            desc(
                { "You are in a low tunnel." }, {
                    "You are in a low low tunnel leading into a cave. " +
                            "The cave continues to the west. There is ${
                                when (gateIsUnlocked.isTrue) {
                                    true -> "an unlocked gate"
                                    false -> "a locked gate"
                                }
                            } to the east."
                })
            action("unlock", "gate") {
                if (inventoryHas("keys")) {
                    say("You find the gate key on the ring.!")
                    gateIsUnlocked.set(true)
                } else {
                    say("Let's think ... what do we need to unlock a lock?")
                }
            }
            action("lock", "gate") {
                if (inventoryHas("keys")) {
                    say("Familiar as you are with the keys, you quickly lock the gate.")
                    gateIsUnlocked.set(false)
                } else {
                    say("Without the key?")
                }
            }
            go(D.East, R.CaveEntrance) {
                when (gateIsUnlocked.truth) {
                    true -> yes("You swing open the unlocked gate and leave the cave.")
                    false -> no("The gate is locked. Haven't we discussed this?")
                }
            }
        }
    }
    return theWorld
}

class MainView : View() {
    private val world = makeGameWorld()
    private val player = Player(world, R.Spring)

    override val root: Parent = vbox {
        minWidth = 400.0
        minHeight = 200.0
        myText = textarea(
            textContents + "\n"
                    + player.currentRoom.description() + ".\n"
                    + player.currentRoom.itemString()
        ) {
            isEditable = false
            isWrapText = true
            style { fontSize = 20.px }
            vgrow = Priority.ALWAYS
        }
        myCommand = textfield("") {
            action { someoneTyped() }
            promptText = "Command"
            style { fontSize = 20.px }
        }
    }

    private fun someoneTyped() {
        val cmd = myCommand.text
        val resultString = player.command(cmd)
        myText.appendText("\n> $cmd")
        myText.appendText("\n" + resultString)
        myCommand.text = ""
        myCommand.appendText("")
    }
}

class HelloApplication : App(MainView::class)

fun main() {
    launch<HelloApplication>()
}