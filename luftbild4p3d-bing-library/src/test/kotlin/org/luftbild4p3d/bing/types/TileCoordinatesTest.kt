package org.luftbild4p3d.bing.types

import io.kotlintest.matchers.haveSize
import io.kotlintest.matchers.should
import io.kotlintest.matchers.shouldBe
import io.kotlintest.matchers.shouldEqual
import io.kotlintest.specs.StringSpec
import org.luftbild4p3d.bing.types.LevelOfDetail.LOD14
import org.luftbild4p3d.bing.types.LevelOfDetail.LOD18
import io.kotlintest.properties.*

class TileCoordinatesTest : StringSpec({

    "toPixelCoordinates converts coordinates of LOD14 to pixel coordinates" {
        val cases = table(
                headers("x", "y", "pixelX", "pixelY"),
                row(0, 0, 0, 0),
                row(8192, 8192, 2097152, 2097152),
                row(16383, 16383, 4194048, 4194048)
        )

        forAll(cases) { x, y, pixelX, pixelY ->
            TileCoordinates(x, y, LOD14).toPixelCoordinates() shouldBe PixelCoordinates(pixelX, pixelY, LOD14)
        }
    }

    "toPixelCoordinates converts coordinates of LOD18 to pixel coordinates" {
        val cases = table(
                headers("x", "y", "pixelX", "pixelY"),
                row(0, 0, 0, 0),
                row(131072, 131072, 33554432, 33554432),
                row(262143, 262143, 67108608, 67108608)
        )

        forAll(cases) { x, y, pixelX, pixelY ->
            TileCoordinates(x, y, LOD18).toPixelCoordinates() shouldBe PixelCoordinates(pixelX, pixelY, LOD18)
        }
    }

    "toQuadKey converts coordinates of LOD14 to quad key" {
        val cases = table(
                headers("x", "y", "quadKey"),
                row(0, 0, "00000000000000"),
                row(16383, 0, "11111111111111"),
                row(8192, 8192, "30000000000000"),
                row(0, 16383, "22222222222222"),
                row(16383, 16383, "33333333333333")
        )

        forAll(cases) { x, y, quadKey ->
            TileCoordinates(x, y, LOD14).toQuadKey() shouldBe quadKey
        }
    }

    "toQuadKey converts coordinates of LOD18 to quad key" {
        val cases = table(
                headers("x", "y", "quadKey"),
                row(0, 0, "000000000000000000"),
                row(262143, 0, "111111111111111111"),
                row(131072, 131072, "300000000000000000"),
                row(0, 262143, "222222222222222222"),
                row(262143, 262143, "333333333333333333")
        )

        forAll(cases) { x, y, quadKey ->
            TileCoordinates(x, y, LOD18).toQuadKey() shouldBe quadKey
        }
    }

    "getTileCoordinatesGenerator returns a function that generates a list of tile coordinates from starting coordinates with size numberOfColumns * numberOfRows" {
        val startTile = TileCoordinates(1, 2, LevelOfDetail.LOD16)

        val generateTileCoordinates = getTileCoordinatesGenerator(2, 3)
        val actual = generateTileCoordinates(startTile)

        actual should haveSize(2 * 3)
    }

    "getTileCoordinatesGenerator returns a function that generates a list of tile coordinatess where each x,y coordinates is translated by offset" {
        val startTile = TileCoordinates(1, 2, LevelOfDetail.LOD16)

        val generateTileCoordinates = getTileCoordinatesGenerator(2, 3, 5)
        val actual = generateTileCoordinates(startTile)

        actual[0] shouldEqual startTile
        actual[1] shouldEqual TileCoordinates(6, 2, LevelOfDetail.LOD16)
        actual[2] shouldEqual TileCoordinates(1, 7, LevelOfDetail.LOD16)
        actual[3] shouldEqual TileCoordinates(6, 7, LevelOfDetail.LOD16)
        actual[4] shouldEqual TileCoordinates(1, 12, LevelOfDetail.LOD16)
        actual[5] shouldEqual TileCoordinates(6, 12, LevelOfDetail.LOD16)
    }

})
