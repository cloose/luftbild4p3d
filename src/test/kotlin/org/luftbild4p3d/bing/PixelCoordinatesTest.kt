package org.luftbild4p3d.bing

import io.kotlintest.matchers.shouldEqual
import io.kotlintest.specs.StringSpec

class PixelCoordinatesTest : StringSpec({

    "toTile converts coordinates at meridian and equator to tile of LOD14" {
        val actual = PixelCoordinates(2097152, 2097152, LevelOfDetail.LOD14).toTile()

        actual.x shouldEqual 8192
        actual.y shouldEqual 8192
        actual.levelOfDetail shouldEqual LevelOfDetail.LOD14
    }

    "toTile converts coordinates at north west border to tile of LOD14" {
        val actual = PixelCoordinates(0, 0, LevelOfDetail.LOD14).toTile()

        actual.x shouldEqual 0
        actual.y shouldEqual 0
        actual.levelOfDetail shouldEqual LevelOfDetail.LOD14
    }

    "toTile converts coordinates at south east border to tile of LOD14" {
        val actual = PixelCoordinates(4194303, 4194303, LevelOfDetail.LOD14).toTile()

        actual.x shouldEqual 16383
        actual.y shouldEqual 16383
        actual.levelOfDetail shouldEqual LevelOfDetail.LOD14
    }

    "toWgs84Coordinates converts coordinates at meridian and equator to WGS84 coordinates of LOD14" {
        val actual = PixelCoordinates(2097152, 2097152, LevelOfDetail.LOD14).toWgs84Coordinates()

        actual.latitude shouldEqual 0.0
        actual.longitude shouldEqual 0.0
    }

    "toWgs84Coordinates converts coordinates at north west border to WGS84 coordinates of LOD14" {
        val actual = PixelCoordinates(0, 0, LevelOfDetail.LOD14).toWgs84Coordinates()

        actual.latitude shouldEqual 85.05112877980659
        actual.longitude shouldEqual -180.0
    }

    "toWgs84Coordinates converts coordinates at south east border to WGS84 coordinates of LOD14" {
        val actual = PixelCoordinates(4194304, 4194304, LevelOfDetail.LOD14).toWgs84Coordinates()

        actual.latitude shouldEqual -85.05112137546755
        actual.longitude shouldEqual 179.99991416931152
    }

})