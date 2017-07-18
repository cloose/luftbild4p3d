package org.luftbild4p3d.app

import io.reactivex.schedulers.Schedulers
import javafx.application.Platform
import javafx.geometry.Pos
import javafx.scene.control.Spinner
import javafx.scene.control.TextArea
import javafx.scene.layout.Priority
import org.luftbild4p3d.bing.LevelOfDetail
import tornadofx.*

class MainView : View() {

    var latitudeSpinner: Spinner<Int> by singleAssign()
    var longitudeSpinner: Spinner<Int> by singleAssign()
    var outputLogTextArea: TextArea by singleAssign()

    override val root = form {
        fieldset("WGS84 Coordinates") {
            field("Latitude:") {
                spinner(-85, 85, 54, 1) {
                    latitudeSpinner = this
                }
            }
            field("Longitude:") {
                spinner(-180, 180, 10, 1) {
                    longitudeSpinner = this
                }
            }
        }
        fieldset("Output Log") {
            field {
                textarea {
                    outputLogTextArea = this
                    vgrow = Priority.ALWAYS
                    isEditable = false
                    isWrapText = true
                }
            }
        }
        buttonbar {
            button("Generate Tile") {
                alignment = Pos.BASELINE_RIGHT
                action {
                    isDisable = true

                    val area = Area(latitudeSpinner.value, longitudeSpinner.value, LevelOfDetail.LOD16)
                    processArea(area, { line -> Platform.runLater { outputLogTextArea.appendText(line + "\n") } }).subscribeOn(Schedulers.newThread()).subscribe { isDisable = false }
                }
            }

        }
    }
}
