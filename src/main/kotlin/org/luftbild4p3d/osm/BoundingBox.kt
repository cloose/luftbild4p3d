package org.luftbild4p3d.osm

import org.luftbild4p3d.bing.Tile

class BoundingBox(val startLatitude: Double, val startLongitude: Double, val endLatitude: Double, val endLongitude: Double) {

    companion object {
        fun create(topLeftTile: Tile, numberOfTilesPerRowAndColumn: Int): BoundingBox {
            val bottomRightTile = Tile(topLeftTile.x + numberOfTilesPerRowAndColumn, topLeftTile.y + numberOfTilesPerRowAndColumn, topLeftTile.levelOfDetail)

            val topLeftCoordinates = topLeftTile.toPixelCoordinates().toWgs84Coordinates()
            val bottomRightCoordinates = bottomRightTile.toPixelCoordinates().toWgs84Coordinates()

            val startLatitude = minOf(topLeftCoordinates.latitude, bottomRightCoordinates.latitude)
            val startLongitude = minOf(topLeftCoordinates.longitude, bottomRightCoordinates.longitude)
            val endLatitude = maxOf(topLeftCoordinates.latitude, bottomRightCoordinates.latitude)
            val endLongitude = maxOf(topLeftCoordinates.longitude, bottomRightCoordinates.longitude)

            return BoundingBox(startLatitude, startLongitude, endLatitude, endLongitude)
        }
    }

    override fun toString() = "$startLatitude,$startLongitude,$endLatitude,$endLongitude"

}