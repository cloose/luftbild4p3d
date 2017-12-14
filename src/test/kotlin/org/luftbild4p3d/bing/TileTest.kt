package org.luftbild4p3d.bing

import io.kotlintest.matchers.haveSize
import io.kotlintest.matchers.should
import io.kotlintest.matchers.shouldEqual
import io.kotlintest.specs.StringSpec
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class TileTest : StringSpec ({

    "toPixelCoordinates converts tile at meridian and equator to pixel coordinates of LOD14" {
        val actual = Tile(8192, 8192, LevelOfDetail.LOD14).toPixelCoordinates()

        actual.x shouldEqual 2097152
        actual.y shouldEqual 2097152
        actual.levelOfDetail shouldEqual LevelOfDetail.LOD14
    }

    "toPixelCoordinates converts tile at north west border to pixel coordinates of LOD14" {
        val actual = Tile(0, 0, LevelOfDetail.LOD14).toPixelCoordinates()

        actual.x shouldEqual 0
        actual.y shouldEqual 0
        actual.levelOfDetail shouldEqual LevelOfDetail.LOD14
    }

    "toPixelCoordinates converts tile at south east border to pixel coordinates of LOD14" {
        val actual = Tile(16383, 16383, LevelOfDetail.LOD14).toPixelCoordinates()

        actual.x shouldEqual 4194048
        actual.y shouldEqual 4194048
        actual.levelOfDetail shouldEqual LevelOfDetail.LOD14
    }

    "toQuadKey converts tile at meridian and equator to quad key of LOD14" {
        val actual = Tile(8192, 8192, LevelOfDetail.LOD14).toQuadKey()

        actual shouldEqual "30000000000000"
    }

    "toQuadKey converts tile at north west border to quad key of LOD14" {
        val actual = Tile(0, 0, LevelOfDetail.LOD14).toQuadKey()

        actual shouldEqual "00000000000000"
    }

    "toQuadKey converts tile at south east border to quad key of LOD14" {
        val actual = Tile(16383, 16383, LevelOfDetail.LOD14).toQuadKey()

        actual shouldEqual "33333333333333"
    }

    "generateTiles returns list of tiles with size numberOfColumns * numberOfRows" {
        val startTile = Tile(1, 2, LevelOfDetail.LOD16)

        val actual = generateTiles(startTile, 2, 3, 5)

        actual should haveSize(2 * 3)
    }

    "generateTiles returns list of tiles where each x,y coordinates is translated by offset" {
        val startTile = Tile(1, 2, LevelOfDetail.LOD16)

        val actual = generateTiles(startTile, 2, 3, 5)

        actual[0] shouldEqual startTile
        actual[1] shouldEqual Tile(6, 2, LevelOfDetail.LOD16)
        actual[2] shouldEqual Tile(1, 7, LevelOfDetail.LOD16)
        actual[3] shouldEqual Tile(6, 7, LevelOfDetail.LOD16)
        actual[4] shouldEqual Tile(1, 12, LevelOfDetail.LOD16)
        actual[5] shouldEqual Tile(6, 12, LevelOfDetail.LOD16)
    }

    "downloadTile returns a function that downloads a tile from a URL" {
        val imageFile = File("test.bmp")
        createTestImage(imageFile, Color.RED, Color.GREEN)
        val expectedTile = Tile(1, 2, LevelOfDetail.LOD16)
        var actualTile : Tile? = null

        val toMapTileImageUrl = { tile: Tile ->
            actualTile = tile
            imageFile.toURI().toURL()
        }

        val actualImage = downloadTile(toMapTileImageUrl)(expectedTile)

        actualTile shouldEqual expectedTile
        actualImage.getRGB(0, 0) shouldEqual Color.RED.rgb
        actualImage.getRGB(1, 0) shouldEqual Color.GREEN.rgb

        imageFile.delete()
    }
})

fun createTestImage(file: File, firstPixel: Color, secondPixel: Color) {
    val image = BufferedImage(2, 1, BufferedImage.TYPE_INT_RGB)
    image.setRGB(0, 0, firstPixel.rgb)
    image.setRGB(1, 0, secondPixel.rgb)

    ImageIO.write(image, "bmp", file)
}