package org.luftbild4p3d.bing

import org.junit.Test
import kotlin.test.assertEquals

class TileTest {

    @Test
    fun convertsTileAtMeridianAndEquatorToPixelCoordinatesForLOD14() {
        val actual = Tile(8192, 8192, LevelOfDetail.LOD14).toPixelCoordinates()

        assertEquals(2097152, actual.x)
        assertEquals(2097152, actual.y)
        assertEquals(LevelOfDetail.LOD14, actual.levelOfDetail)
    }

    @Test
    fun convertsNorthWestBoundToPixelCoordinatesForLOD14() {
        val actual = Tile(0, 0, LevelOfDetail.LOD14).toPixelCoordinates()

        assertEquals(0, actual.x)
        assertEquals(0, actual.y)
        assertEquals(LevelOfDetail.LOD14, actual.levelOfDetail)
    }

    @Test
    fun convertsSouthEastBoundToPixelCoordinatesForLOD14() {
        val actual = Tile(16383, 16383, LevelOfDetail.LOD14).toPixelCoordinates()

        assertEquals(4194048, actual.x)
        assertEquals(4194048, actual.y)
        assertEquals(LevelOfDetail.LOD14, actual.levelOfDetail)
    }

    @Test
    fun convertsTileAtMeridianAndEquatorToQuadKeyForLOD14() {
        val actual = Tile(8192, 8192, LevelOfDetail.LOD14).toQuadKey()

        assertEquals("30000000000000", actual)
    }

    @Test
    fun convertsNorthWestBoundToQuadKeyForLOD14() {
        val actual = Tile(0, 0, LevelOfDetail.LOD14).toQuadKey()

        assertEquals("00000000000000", actual)
    }

    @Test
    fun convertsSouthEastBoundToQuadKeyForLOD14() {
        val actual = Tile(16383, 16383, LevelOfDetail.LOD14).toQuadKey()

        assertEquals("33333333333333", actual)
    }

}