package org.luftbild4p3d.bing.types

import org.luftbild4p3d.bing.clippedTo
import java.lang.Math.*

data class Wgs84Coordinates(val latitude: Double, val longitude: Double) {

    fun toPixelCoordinates(levelOfDetail: LevelOfDetail): PixelCoordinates {
        val clipToMapSize = { value: Double -> value.clippedTo(0.0, levelOfDetail.mapSize.toDouble()-1).toInt() }

        val sinLatitude = sin(toRadians(latitude))

        val x = ((longitude + 180.0) / 360.0) * levelOfDetail.mapSize + 0.5
        val y = (0.5 - log((1 + sinLatitude) / (1 - sinLatitude)) / (4 * PI)) * levelOfDetail.mapSize + 0.5

        return PixelCoordinates(clipToMapSize(x), clipToMapSize(y), levelOfDetail)
    }

}