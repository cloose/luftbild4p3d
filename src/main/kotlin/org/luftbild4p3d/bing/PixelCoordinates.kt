package org.luftbild4p3d.bing

fun Int.clippedTo(minValue: Double, maxValue: Double) = Math.min(Math.max(this.toDouble(), minValue), maxValue)

data class PixelCoordinates(val x: Int, val y: Int, val levelOfDetail: LevelOfDetail) {

    fun toTile(): Tile {
        val x = Math.floor(x.toDouble() / Tile.SIZE).toInt()
        val y = Math.floor(y.toDouble() / Tile.SIZE).toInt()
        return Tile(x, y, levelOfDetail)
    }

    fun toWgs84Coordinates(): Wgs84Coordinates {
        val mapSize = Tile.SIZE shl levelOfDetail.bingLOD

        val x = (x.clippedTo(0.0, mapSize - 1.0) / mapSize) - 0.5
        val y = 0.5 - (y.clippedTo(0.0, mapSize - 1.0) / mapSize)

        var latitude = 90 - 360 * Math.atan(Math.exp(-y * 2 * Math.PI)) / Math.PI
        var longitude = 360 * x

        return Wgs84Coordinates(latitude, longitude)
    }

}