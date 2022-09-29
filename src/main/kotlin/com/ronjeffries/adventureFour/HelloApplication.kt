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
        room("spring") {
            desc("spring", "You are at a clear water spring. " +
                    "There is a well house to the east, and a wooded area to the west and south.")
            item("water") {
                desc("water", "Perfectly ordinary sparkling spring water, " +
                        "with a tinge or iron that has leached from the surrounding rocks, " +
                        "and a minor tang of fluoride, which is good for your teeth.)")
            }
            go(D.east, R.wellhouse)
            go(D.west, R.woods)
            go(D.south, R.`woods toward cave`)
            action("take", "water") { imp->
                if (inventoryHas("bottle")) {
                    inventorySetInformation("bottle", " of water")
                    say("You fill your bottle with water.")
                } else {
                    imp.say("What would you keep it in?") }
            }
        }
        room("woods") {
            desc("woods", "You are in a dark and forbidding wooded area. " +
                    "It's not clear which way to go.")
            go(D.east, R.spring)
            go(D.north, R.woods)
            go(D.west, R.woods)
            go(D.northwest, R.woods)
            go(D.southwest, R.woods)
        }
        room("wellhouse" ) {
            desc("well house", "You are in a small house near a spring. " +
                    "The house is sparsely decorated, in a rustic style. " +
                    "It appears to be well kept.")
            item("bottle")
            item("keys")
            go(D.west, R.spring)
        }
        room("woods toward cave") {
            desc("breezy woods", "You are in the woods. " +
                    "There is a cool breeze coming from the west.")
            go("w","cave entrance")
            go("n", "spring")
        }
        room("cave entrance") {
            desc("cave entrance",
                "You are at an entrance to a cave. " +
                        "There is a locked gate blocking your way west.")
            go("e","woods toward cave")
        }
    }
    return theWorld
}

class MainView: View() {
    private val world = makeGameWorld()
    private val player = Player(world, "spring")

    override val root: Parent = vbox {
        minWidth = 400.0
        minHeight = 200.0
        myText = textarea(textContents + "\n"
                +player.currentRoom.description() + ".\n"
                + player.currentRoom.itemString()) {
            isEditable = false
            isWrapText = true
            style { fontSize = 20.px}
            vgrow = Priority.ALWAYS
        }
        myCommand = textfield ("") {
            action { someoneTyped() }
            promptText = "Command"
            style { fontSize = 20.px}
        }
    }

    private fun someoneTyped() {
        val cmd = myCommand.text
        val resultString = player.command(cmd)
        myText.appendText("\n> $cmd")
        myText.appendText("\n"+resultString)
        myCommand.text = ""
        myCommand.appendText("")
    }
}



class HelloApplication : App(MainView::class)

fun main() {
    launch<HelloApplication>()
}