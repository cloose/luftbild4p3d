package org.luftbild4p3d.bing.types

import org.luftbild4p3d.bing.clippedTo

data class PixelCoordinates(val x: Int, val y: Int, val levelOfDetail: LevelOfDetail) {

    fun toWgs84Coordinates(): Wgs84Coordinates {
        val clipToMapSize = { value: Int -> value.clippedTo(0, (levelOfDetail.mapSize-1).toInt()).toDouble() }

        val clippedX = (clipToMapSize(x) / levelOfDetail.mapSize) - 0.5
        val clippedY = 0.5 - (clipToMapSize(y) / levelOfDetail.mapSize)

        val latitude = 90 - 360 * Math.atan(Math.exp(-clippedY * 2 * Math.PI)) / Math.PI
        var longitude = 360 * clippedX

        return Wgs84Coordinates(latitude, longitude)
    }

    fun toTileCoordinates(): TileCoordinates {
        return TileCoordinates(x / 256, y / 256, levelOfDetail)
    }

}