package org.luftbild4p3d.bing

import org.junit.Test
import kotlin.test.assertEquals

class Wgs84CoordinatesTest {

    @Test
    fun convertsAngleInDegreesToRadians() {
        assertEquals(0.0, 0.0.toRadians())
        assertEquals(Math.PI / 2, 90.0.toRadians())
        assertEquals(Math.PI, 180.0.toRadians())
        assertEquals(Math.PI * 3 / 2, 270.0.toRadians())
        assertEquals(Math.PI * 2, 360.0.toRadians())
    }

    @Test
    fun convertsCoordinatesAtMeridianAndEquatorToPixelCoordinatesForLOD14() {
        val actual = Wgs84Coordinates(0.0, 0.0).toPixelCoordinates(LevelOfDetail.LOD14)

        assertEquals(2097152, actual.x)
        assertEquals(2097152, actual.y)
        assertEquals(LevelOfDetail.LOD14, actual.levelOfDetail)
    }

    @Test
    fun convertsNorthWestBoundToPixelCoordinatesForLOD14() {
        val actual = Wgs84Coordinates(85.05112878, -180.0).toPixelCoordinates(LevelOfDetail.LOD14)

        assertEquals(0, actual.x)
        assertEquals(0, actual.y)
        assertEquals(LevelOfDetail.LOD14, actual.levelOfDetail)
    }

    @Test
    fun convertsSouthEastBoundToPixelCoordinatesForLOD14() {
        val actual = Wgs84Coordinates(-85.05112878, 180.0).toPixelCoordinates(LevelOfDetail.LOD14)

        assertEquals(4194304, actual.x)
        assertEquals(4194304, actual.y)
        assertEquals(LevelOfDetail.LOD14, actual.levelOfDetail)
    }

    @Test
    fun convertsCoordinatesAtMeridianAndEquatorToPixelCoordinatesForLOD18() {
        val actual = Wgs84Coordinates(0.0, 0.0).toPixelCoordinates(LevelOfDetail.LOD18)

        assertEquals(33554432, actual.x)
        assertEquals(33554432, actual.y)
        assertEquals(LevelOfDetail.LOD18, actual.levelOfDetail)
    }

    @Test
    fun convertsNorthWestBoundToPixelCoordinatesForLOD18() {
        val actual = Wgs84Coordinates(85.05112878, -180.0).toPixelCoordinates(LevelOfDetail.LOD18)

        assertEquals(0, actual.x)
        assertEquals(0, actual.y)
        assertEquals(LevelOfDetail.LOD18, actual.levelOfDetail)
    }

    @Test
    fun convertsSouthEastBoundToPixelCoordinatesForLOD18() {
        val actual = Wgs84Coordinates(-85.05112878, 180.0).toPixelCoordinates(LevelOfDetail.LOD18)

        assertEquals(67108864, actual.x)
        assertEquals(67108864, actual.y)
        assertEquals(LevelOfDetail.LOD18, actual.levelOfDetail)
    }

}