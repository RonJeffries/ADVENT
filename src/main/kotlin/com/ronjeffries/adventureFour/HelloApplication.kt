package com.ronjeffries.adventureFour

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.stage.Stage
import tornadofx.*
import tornadofx.Stylesheet.Companion.label

var myLabel: Label by singleAssign()

class MainView: View() {
    override val root: Parent = vbox {
        minWidth = 400.0
        minHeight = 200.0
        myLabel = label("Hello Ron")
        button("Press me") {
            action { someonePressedMe() }
        }
    }
}

fun someonePressedMe() {
    println("pressed")
    myLabel.text = "I saw that!"
}

class HelloApplication : App(MainView::class) {
}

fun main() {
    launch<HelloApplication>()
}