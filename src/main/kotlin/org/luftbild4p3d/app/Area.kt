package org.luftbild4p3d.app

import org.luftbild4p3d.bing.LevelOfDetail
import org.luftbild4p3d.bing.Wgs84Coordinates

data class Area(val latitude: Int, val longitude: Int, val levelOfDetail: LevelOfDetail) {

    val startTile = Wgs84Coordinates(latitude.toDouble(), longitude.toDouble()).toPixelCoordinates(levelOfDetail).toTile()

    companion object {
        val BGL_FILES_PER_ROW_AND_COLUMN = 1//4
        val IMAGES_PER_BGL_FILE_ROW_AND_COLUMN = 2//3
        val TILES_PER_IMAGE_ROW_AND_COLUMN = 16
    }
}