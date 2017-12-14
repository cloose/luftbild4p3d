package org.luftbild4p3d.bing

import io.kotlintest.matchers.shouldEqual
import io.kotlintest.specs.StringSpec

class Wgs84CoordinatesTest : StringSpec({

    "toRadians converts angle in degrees to radians" {
        0.0.toRadians() shouldEqual 0.0
        90.0.toRadians() shouldEqual Math.PI / 2
        180.0.toRadians() shouldEqual Math.PI
        270.0.toRadians() shouldEqual Math.PI * 3 / 2
        360.0.toRadians() shouldEqual Math.PI * 2
    }

    "toPixelCoordinates converts coordinates at meridian and equator to pixel coordinates of LOD14" {
        val actual = Wgs84Coordinates(0.0, 0.0).toPixelCoordinates(LevelOfDetail.LOD14)

        actual.x shouldEqual 2097152
        actual.y shouldEqual 2097152
        actual.levelOfDetail shouldEqual LevelOfDetail.LOD14
    }

    "toPixelCoordinates converts coordinates at north west border to pixel coordinates of LOD14" {
        val actual = Wgs84Coordinates(85.05112878, -180.0).toPixelCoordinates(LevelOfDetail.LOD14)

        actual.x shouldEqual 0
        actual.y shouldEqual 0
        actual.levelOfDetail shouldEqual LevelOfDetail.LOD14
    }

    "toPixelCoordinates converts coordinates at south east border to pixel coordinates of LOD14" {
        val actual = Wgs84Coordinates(-85.05112878, 180.0).toPixelCoordinates(LevelOfDetail.LOD14)

        actual.x shouldEqual 4194304
        actual.y shouldEqual 4194304
        actual.levelOfDetail shouldEqual LevelOfDetail.LOD14
    }

    "toPixelCoordinates converts coordinates at meridian and equator to pixel coordinates of LOD18" {
        val actual = Wgs84Coordinates(0.0, 0.0).toPixelCoordinates(LevelOfDetail.LOD18)

        actual.x shouldEqual 33554432
        actual.y shouldEqual 33554432
        actual.levelOfDetail shouldEqual LevelOfDetail.LOD18
    }

    "toPixelCoordinates converts coordinates at north west border to pixel coordinates of LOD18" {
        val actual = Wgs84Coordinates(85.05112878, -180.0).toPixelCoordinates(LevelOfDetail.LOD18)

        actual.x shouldEqual 0
        actual.y shouldEqual 0
        actual.levelOfDetail shouldEqual LevelOfDetail.LOD18
    }

    "toPixelCoordinates converts coordinates at south east border to pixel coordinates of LOD18" {
        val actual = Wgs84Coordinates(-85.05112878, 180.0).toPixelCoordinates(LevelOfDetail.LOD18)

        actual.x shouldEqual 67108864
        actual.y shouldEqual 67108864
        actual.levelOfDetail shouldEqual LevelOfDetail.LOD18
    }

})