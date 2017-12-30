package org.luftbild4p3d.bing.types

import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec
import org.luftbild4p3d.bing.types.LevelOfDetail.LOD14
import org.luftbild4p3d.bing.types.LevelOfDetail.LOD18

class Wgs84CoordinatesTest : StringSpec ({

    "toPixelCoordinates converts coordinates at meridian and equator to pixel coordinates of LOD14" {
        val actual = Wgs84Coordinates(0.0, 0.0).toPixelCoordinates(LOD14)

        actual shouldBe PixelCoordinates((LOD14.mapSize/2).toInt(), (LOD14.mapSize/2).toInt(), LOD14)
    }

    "toPixelCoordinates converts coordinates at north west border to pixel coordinates of LOD14" {
        val actual = Wgs84Coordinates(85.05112878, -180.0).toPixelCoordinates(LOD14)

        actual shouldBe PixelCoordinates(0, 0, LOD14)
    }

    "toPixelCoordinates converts coordinates at south east border to pixel coordinates of LOD14" {
        val actual = Wgs84Coordinates(-85.05112878, 180.0).toPixelCoordinates(LOD14)

        actual shouldBe PixelCoordinates((LOD14.mapSize-1).toInt(), (LOD14.mapSize-1).toInt(), LOD14)
    }

    "toPixelCoordinates converts coordinates at meridian and equator to pixel coordinates of LOD18" {
        val actual = Wgs84Coordinates(0.0, 0.0).toPixelCoordinates(LOD18)

        actual shouldBe PixelCoordinates((LOD18.mapSize/2).toInt(), (LOD18.mapSize/2).toInt(), LOD18)
    }

    "toPixelCoordinates converts coordinates at north west border to pixel coordinates of LOD18" {
        val actual = Wgs84Coordinates(85.05112878, -180.0).toPixelCoordinates(LOD18)

        actual shouldBe PixelCoordinates(0, 0, LOD18)
    }

    "toPixelCoordinates converts coordinates at south east border to pixel coordinates of LOD18" {
        val actual = Wgs84Coordinates(-85.05112878, 180.0).toPixelCoordinates(LOD18)

        actual shouldBe PixelCoordinates((LOD18.mapSize-1).toInt(), (LOD18.mapSize-1).toInt(), LOD18)
    }

})