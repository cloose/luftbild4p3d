package org.luftbild4p3d.bing

import java.awt.image.BufferedImage
import java.net.URL
import javax.imageio.ImageIO

data class Tile(val x: Int, val y: Int, val levelOfDetail: LevelOfDetail) {

    companion object {
        val SIZE = 256
    }

    fun toPixelCoordinates() = PixelCoordinates(x * SIZE, y * SIZE, levelOfDetail)

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

fun generateTiles(startTile: Tile, numberOfColumns: Int, numberOfRows: Int, offset: Int) : List<Tile> {
    return (0 until numberOfRows).flatMap { y -> (0 until numberOfColumns).map { x -> Tile(startTile.x + offset * x, startTile.y + offset * y, startTile.levelOfDetail) } }
}

fun downloadTile(toMapTileImageUrl : (Tile) -> URL) : (Tile) -> BufferedImage {
    return { tile -> ImageIO.read(toMapTileImageUrl(tile)) }
}