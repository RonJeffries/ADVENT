package com.ronjeffries.adventureFour

import javafx.scene.Parent
import javafx.scene.control.TextArea
import tornadofx.*

var myText: TextArea by singleAssign()

var count = 1
var textContents = "Hello Ron"

class MainView: View() {
    override val root: Parent = vbox {
        minWidth = 400.0
        minHeight = 200.0
        myText = textarea(textContents) {
            isEditable = false
        }
        button("Press me") {
            action { someonePressedMe() }
        }
    }
}

fun someonePressedMe() {
    var newLine = "\nI saw that again $count"
    myText.appendText(newLine)
    count++
}

class HelloApplication : App(MainView::class) {
}

fun main() {
    launch<HelloApplication>()
}