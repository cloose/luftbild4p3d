package org.luftbild4p3d.bing.types

data class TileCoordinates(val x: Int, val y: Int, val levelOfDetail: LevelOfDetail) {

    fun toPixelCoordinates(): PixelCoordinates {
        return PixelCoordinates(x * 256, y * 256, levelOfDetail)
    }

    fun toQuadKey(): String {
        val quadKey = StringBuilder()

        for (i in levelOfDetail.bingLOD downTo 1) {
            var digit = '0'
            val mask = 1 shl (i - 1)

            if ((x and mask) != 0) {
                digit++
            }

            if ((y and mask) != 0) {
                digit++
                digit++
            }

            quadKey.append(digit)
        }

        return quadKey.toString()
    }

}

fun getTileCoordinatesGenerator(numberOfColumns: Int, numberOfRows: Int, offset: Int = 1) : (TileCoordinates) -> List<TileCoordinates> {
    return { startTileCoordinates ->
        (0 until numberOfRows).flatMap { y ->
            (0 until numberOfColumns).map { x ->
                TileCoordinates(startTileCoordinates.x + offset * x, startTileCoordinates.y + offset * y, startTileCoordinates.levelOfDetail) } }
    }
}