package org.luftbild4p3d.app

import javafx.geometry.Pos
import org.luftbild4p3d.bing.LevelOfDetail
import tornadofx.*

class AreaEditor : View() {

    private val viewModel: AreaViewModel by inject()

    override val root = form {
        fieldset {
            field("Latitude:") {
                spinner(-85, 85, 54, 1, false, viewModel.latitude)
            }
            field("Longitude:") {
                spinner(-180, 180, 10, 1, false, viewModel.longitude)
            }
            field("Level of Detail:") {
                combobox(viewModel.levelOfDetail, LevelOfDetail.values().asList())
            }
        }
        buttonbar {
            button("Generate Tile") {
                alignment = Pos.BASELINE_RIGHT
                onAction = viewModel.onGenerateTileClicked
            }
        }
    }

}