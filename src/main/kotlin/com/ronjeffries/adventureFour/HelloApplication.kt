package com.ronjeffries.adventureFour

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import tornadofx.App
import tornadofx.View
import tornadofx.launch
import tornadofx.pane

class MainView: View() {
    override val root: Parent = pane()
}

class HelloApplication : App(MainView::class) {
}

fun main() {
    launch<HelloApplication>()
}