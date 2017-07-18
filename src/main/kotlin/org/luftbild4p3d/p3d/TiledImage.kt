package org.luftbild4p3d.p3d

import org.luftbild4p3d.bing.Tile

class TiledImage(val topLeftTile: Tile, val numberOfTiles: Int) {

    val latitude = topLeftTile.toPixelCoordinates().toWgs84Coordinates().latitude
    val longitude = topLeftTile.toPixelCoordinates().toWgs84Coordinates().longitude
    val size = Tile.SIZE * numberOfTiles

    fun leftMostTileOfRow(row: Int) = Tile(topLeftTile.x, topLeftTile.y + row, topLeftTile.levelOfDetail)

    fun pixelSizeX(): Double {
        val longitudeOfRightTile = Tile(topLeftTile.x + numberOfTiles, topLeftTile.y, topLeftTile.levelOfDetail).toPixelCoordinates().toWgs84Coordinates().longitude
        return Math.abs(longitudeOfRightTile - longitude) / size
    }

    fun pixelSizeY(): Double {
        val latitudeOfBelowTile = Tile(topLeftTile.x, topLeftTile.y + numberOfTiles, topLeftTile.levelOfDetail).toPixelCoordinates().toWgs84Coordinates().latitude
        return Math.abs(latitudeOfBelowTile - latitude) / size
    }

}