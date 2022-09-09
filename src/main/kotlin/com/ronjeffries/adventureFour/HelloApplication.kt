package com.ronjeffries.adventureFour

import javafx.scene.Parent
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.Priority
import tornadofx.*

var myText: TextArea by singleAssign()
var myCommand: TextField by singleAssign()
var textContents = "Welcome to Tiny Adventure!"

class MainView: View() {
    private val world = world {
        room("wellhouse") {
            desc("You're at wellhouse", "You're in a charming wellhouse")
            item("keys")
            item("bottle")
            item("water")
            go("n", "wellhouse")
            go("s", "clearing")
            go("e", "cows")
        }
        room("clearing") {
            desc("You're in a clearing",
                "You're in a charming clearing. There is a fence to the east.")
            item("cows")
            go("n", "wellhouse")
            go("s","clearing")
            go("e", "cows") {
                it.say("You can't climb the fence!")
                false
            }
        }
        room("cows") {
            desc("You're in with the cows.", "You're in a pasture with some cows.")
            go("w", "wellhouse")
            action("cows") { imp -> say("Leave those cows alone") }
        }
    }
    private val player = Player(world, "wellhouse")

    override val root: Parent = vbox {
        minWidth = 400.0
        minHeight = 200.0
        myText = textarea(textContents + "\n"
                +player.currentRoom.longDesc + ".\n"
                + player.currentRoom.itemString()) {
            isEditable = false
            vgrow = Priority.ALWAYS
        }
        myCommand = textfield ("") {
            action { someoneTyped() }
            promptText = "Command"
        }
    }

    private fun someoneTyped() {
        val cmd = myCommand.text
        player.command(cmd)
        myText.appendText("\n> $cmd")
        myText.appendText("\n"+player.resultString)
        myCommand.text = ""
        myCommand.appendText("")
    }
}



class HelloApplication : App(MainView::class)

fun main() {
    launch<HelloApplication>()
}