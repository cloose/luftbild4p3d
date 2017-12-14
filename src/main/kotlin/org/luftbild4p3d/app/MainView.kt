package org.luftbild4p3d.app

import tornadofx.View
import tornadofx.borderpane

class MainView : View() {

    private val areaEditor: AreaEditor by inject()

    override val root = borderpane {
        center = areaEditor.root
    }

}