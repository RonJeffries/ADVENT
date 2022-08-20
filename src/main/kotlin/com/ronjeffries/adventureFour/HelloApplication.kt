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
    override val root: Parent = vbox {
        minWidth = 400.0
        minHeight = 200.0
        myText = textarea(textContents) {
            isEditable = false
        }
        myCommand = textfield ("> ") {
            action { someoneTyped() }
        }
    }
}

fun someoneTyped() {
    var newLine = "\nThat was command $count"
    myText.appendText("\n" + myCommand.text)
    myText.appendText(newLine)
    myCommand.text = "> "
    myCommand.appendText("")
    count++
}

class HelloApplication : App(MainView::class) {
}

fun main() {
    launch<HelloApplication>()
}