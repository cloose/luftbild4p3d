package org.luftbild4p3d.app

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import org.luftbild4p3d.bing.LevelOfDetail
import tornadofx.getValue
import tornadofx.setValue

class AreaModel(latitude: Int = 54, longitude: Int = 10, levelOfDetail: LevelOfDetail = LevelOfDetail.LOD16) {

    val latitudeProperty = SimpleIntegerProperty(this, "latitude", latitude)
    var latitude by latitudeProperty

    val longitudeProperty = SimpleIntegerProperty(this, "longitude", longitude)
    var longitude by longitudeProperty

    val levelOfDetailProperty = SimpleObjectProperty<LevelOfDetail>(this, "levelOfDetail", levelOfDetail)
    var levelOfDetail by levelOfDetailProperty


}
