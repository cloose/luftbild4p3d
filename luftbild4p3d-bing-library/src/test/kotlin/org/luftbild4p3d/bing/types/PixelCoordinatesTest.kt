package org.luftbild4p3d.bing.types

import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec
import org.luftbild4p3d.bing.types.LevelOfDetail.LOD14
import org.luftbild4p3d.bing.types.LevelOfDetail.LOD18
import io.kotlintest.properties.*

class PixelCoordinatesTest : StringSpec({

    "toTileCoordinates converts coordinates of LOD14 to tile coordinates" {
        val cases = table(
                headers("x", "y", "tileX", "tileY"),
                row(0, 0, 0, 0),
                row(2097152, 2097152, 8192, 8192),
                row(4194303, 4194303, 16383, 16383)
        )

        forAll(cases) { x, y, tileX, tileY ->
            PixelCoordinates(x, y, LOD14).toTileCoordinates() shouldBe TileCoordinates(tileX, tileY, LOD14)
        }
    }

    "toTileCoordinates converts coordinates of LOD18 to tile coordinates" {
        val cases = table(
                headers("x", "y", "tileX", "tileY"),
                row(0, 0, 0, 0),
                row(33554432, 33554432, 131072, 131072),
                row(67108863, 67108863, 262143, 262143)
        )

        forAll(cases) { x, y, tileX, tileY ->
            PixelCoordinates(x, y, LOD18).toTileCoordinates() shouldBe TileCoordinates(tileX, tileY, LOD18)
        }
    }

    "toWgs84Coordinates converts coordinates of LOD14 to WGS84 coordinates" {
        val cases = table(
                headers("x", "y", "latitude", "longitude"),
                row(0, 0, 85.05112877980659, -180.0),
                row(2097152, 2097152, 0.0, 0.0),
                row(4194303, 4194303, -85.05112137546755, 179.99991416931152)
        )

        forAll(cases) { x, y, latitude, longitude ->
            PixelCoordinates(x, y, LOD14).toWgs84Coordinates() shouldBe Wgs84Coordinates(latitude, longitude)
        }
    }

    "toWgs84Coordinates converts coordinates of LOD18 to WGS84 coordinates" {
        val cases = table(
                headers("x", "y", "latitude", "longitude"),
                row(0, 0, 85.05112877980659, -180.0),
                row(33554432, 33554432, 0.0, 0.0),
                row(67108863, 67108863, -85.05112831703573, 179.99999463558197)
        )

        forAll(cases) { x, y, latitude, longitude ->
            PixelCoordinates(x, y, LOD18).toWgs84Coordinates() shouldBe Wgs84Coordinates(latitude, longitude)
        }
    }

})