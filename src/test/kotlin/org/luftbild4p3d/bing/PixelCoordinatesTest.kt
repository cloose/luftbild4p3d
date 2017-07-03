package org.luftbild4p3d.bing

import org.junit.Test
import kotlin.test.assertEquals

class PixelCoordinatesTest {

    @Test
    fun convertsCoordinatesAtMeridianAndEquatorToTileForLOD14() {
        val actual = PixelCoordinates(2097152, 2097152, LevelOfDetail.LOD14).toTile()

        assertEquals(8192, actual.x)
        assertEquals(8192, actual.y)
        assertEquals(LevelOfDetail.LOD14, actual.levelOfDetail)
    }

    @Test
    fun convertsNorthWestBoundToPixelCoordinatesForLOD14() {
        val actual = PixelCoordinates(0, 0, LevelOfDetail.LOD14).toTile()

        assertEquals(0, actual.x)
        assertEquals(0, actual.y)
        assertEquals(LevelOfDetail.LOD14, actual.levelOfDetail)
    }

    @Test
    fun convertsSouthEastBoundToPixelCoordinatesForLOD14() {
        val actual = PixelCoordinates(4194303, 4194303, LevelOfDetail.LOD14).toTile()

        assertEquals(16383, actual.x)
        assertEquals(16383, actual.y)
        assertEquals(LevelOfDetail.LOD14, actual.levelOfDetail)
    }

    @Test
    fun convertsCoordinatesAtMeridianAndEquatorToWgs84CoordinatesForLOD14() {
        val actual = PixelCoordinates(2097152, 2097152, LevelOfDetail.LOD14).toWgs84Coordinates()

        assertEquals(0.0, actual.latitude)
        assertEquals(0.0, actual.longitude)
    }

    @Test
    fun convertsNorthWestBoundToWgs84CoordinatesForLOD14() {
        val actual = PixelCoordinates(0, 0, LevelOfDetail.LOD14).toWgs84Coordinates()

        assertEquals(85.05112877980659, actual.latitude)
        assertEquals(-180.0, actual.longitude)
    }

    @Test
    fun convertsSouthEastBoundToWgs84CoordinatesForLOD14() {
        val actual = PixelCoordinates(4194304, 4194304, LevelOfDetail.LOD14).toWgs84Coordinates()

        assertEquals(-85.05112137546755, actual.latitude)
        assertEquals(179.99991416931152, actual.longitude)
    }

}