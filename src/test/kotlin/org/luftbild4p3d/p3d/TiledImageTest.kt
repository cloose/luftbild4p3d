package org.luftbild4p3d.p3d

import org.junit.Test
import org.luftbild4p3d.bing.LevelOfDetail
import org.luftbild4p3d.bing.Tile
import kotlin.test.assertEquals

class TiledImageTest {

    @Test
    fun calculatesImageSizeFromTileSizeAndNumberOfTiles() {
        val tiledImage = TiledImage(Tile(34406, 21042, LevelOfDetail.LOD16), 16)

        assertEquals(4096, tiledImage.size)
    }

    @Test
    fun returnsLeftMostTileOfRequestedRow() {
        val tiledImage = TiledImage(Tile(34406, 21042, LevelOfDetail.LOD16), 16)

        val actualTile = tiledImage.leftMostTileOfRow(5)

        assertEquals(Tile(34406, 21047, LevelOfDetail.LOD16), actualTile)
    }

    @Test
    fun returnsPixelSizeInXDirection() {
        val tiledImage = TiledImage(Tile(34406, 21042, LevelOfDetail.LOD16), 16)

        val actualPixelSize = tiledImage.pixelSizeX()

        assertEquals(2.1457672119140625E-5, actualPixelSize)
    }

    @Test
    fun returnsPixelSizeInYDirection() {
        val tiledImage = TiledImage(Tile(34406, 21042, LevelOfDetail.LOD16), 16)

        val actualPixelSize = tiledImage.pixelSizeY()

        assertEquals(1.2619933317812096E-5, actualPixelSize)
    }
}