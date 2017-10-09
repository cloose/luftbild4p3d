package org.luftbild4p3d.app

import io.reactivex.schedulers.Schedulers
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.geometry.Pos
import javafx.scene.control.ComboBox
import javafx.scene.control.Spinner
import javafx.scene.control.TextArea
import javafx.scene.layout.Priority
import org.luftbild4p3d.bing.LevelOfDetail
import tornadofx.*

class MainView : View() {

    val lods = FXCollections.observableArrayList(LevelOfDetail.values().asList())

    var latitudeSpinner: Spinner<Int> by singleAssign()
    var longitudeSpinner: Spinner<Int> by singleAssign()
    var levelOfDetailComboBox: ComboBox<LevelOfDetail> by singleAssign()
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
            field("Level of Detail:") {
                combobox<LevelOfDetail> {
                    items = lods
                    value = LevelOfDetail.LOD16
                    levelOfDetailComboBox = this
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

                    val area = Area(latitudeSpinner.value, longitudeSpinner.value, levelOfDetailComboBox.selectedItem ?: LevelOfDetail.LOD16)
                    processArea(area, { line -> Platform.runLater { outputLogTextArea.appendText(line + "\n") } }).subscribeOn(Schedulers.newThread()).subscribe { isDisable = false }
                }
            }

        }
    }
}
