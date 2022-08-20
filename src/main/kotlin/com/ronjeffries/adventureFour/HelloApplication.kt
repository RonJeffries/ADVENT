package com.ronjeffries.adventureFour

import javafx.scene.Parent
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import tornadofx.*

var myText: TextArea by singleAssign()
var myCommand: TextField by singleAssign()
var count = 1
var textContents = "Welcome to Tiny Adventure!"

class MainView: View() {
    val world = world {
        room("wellhouse") {
            desc("You're at wellhouse", "You're in a charming wellhouse")
            go("n", "wellhouse")
            go("s", "clearing")
        }
        room("clearing") {
            desc("You're in a clearing", "You're in a charming clearing")
            go("n", "wellhouse")
            go("s","clearing")
        }
    }
    val game = Game(world, "wellhouse")

    override val root: Parent = vbox {
        minWidth = 400.0
        minHeight = 200.0
        myText = textarea(textContents + "\n"+game.currentRoom.longDesc) {
            isEditable = false
        }
        myCommand = textfield ("") {
            action { someoneTyped() }
        }
    }
    fun someoneTyped() {
        val cmd = myCommand.text
        game.command(cmd)
        val newLine = "\n"+game.currentRoom.longDesc
        myText.appendText("\n" + cmd)
        myText.appendText(newLine)
        myCommand.text = ""
        myCommand.appendText("")
        count++
    }
}



class HelloApplication : App(MainView::class) {
}

fun main() {
    launch<HelloApplication>()
}