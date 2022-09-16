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
            desc("spring", "You are at a clear water spring. There is a well house to the east, and a wooded area to the west and south.")
            item("water")
            item("bottle")
            go("e", "well house")
            go("w", "woods")
            go("s", "woods toward cave")
            action(Phrase("take", "water")) { imp
                ->  if (inventoryHas("bottle")) {
//                    addToInventory("bottle of water")
//                    removeInventory("empty bottle")
                    contents.add("water")
                    say("You have filled your bottle with water.")
            } else {
                imp.say("What would you keep it in?") }
            }
        }
        room("woods") {
            desc("woods", "You are in a dark and forbidding wooded area. It's not clear which way to go.")
            go("e", "spring")
            go("n", "woods")
            go("2", "woods")
            go("w", "woods")
            go("nw", "woods")
            go("se", "woods")
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
                +player.currentRoom.longDesc + ".\n"
                + player.currentRoom.itemString()) {
            isEditable = false
            isWrapText = true
            style { fontSize = 20.px}
            vgrow = Priority.ALWAYS
        }
        myCommand = textfield ("") {
            action { someoneTyped() }
            promptText = "Command"
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