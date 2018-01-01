package org.luftbild4p3d.p3d

import io.kotlintest.matchers.*
import io.kotlintest.specs.StringSpec
import org.luftbild4p3d.bing.types.LevelOfDetail.LOD16
import org.luftbild4p3d.bing.types.TileCoordinates
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import javax.imageio.ImageIO

class TiledImageTest : StringSpec({

    val aTiledImage = TiledImage("path", TileCoordinates(1, 2, LOD16))

    "fileName is derived from the tile coordinates" {
        val tiledImage = TiledImage("path", TileCoordinates(1, 2, LOD16))

        tiledImage.fileName shouldEqual "BI16_1_2.bmp"
    }

    "getTiledImageCreator returns a function that creates a TiledImage from an image path and tile coordinates" {
        val expectedTileCoordinates = TileCoordinates(1, 2, LOD16)
        val expectedImagePath = "path"

        val createTiledImage = getTiledImageCreator(expectedImagePath)
        val tiledImage = createTiledImage(expectedTileCoordinates)

        tiledImage.imagePath shouldEqual expectedImagePath
        tiledImage.tileCoordinates shouldEqual expectedTileCoordinates
    }

    "generateTileCoordinatesList generates a 16x16 matrix of TileCoordinates from a TiledImage" {
        val expectedTiledImage = TiledImage("path", TileCoordinates(1, 2, LOD16))

        val (list, tiledImage) = generateTileCoordinatesList(expectedTiledImage)

        list should haveSize(16 * 16)
        tiledImage should beTheSameInstanceAs(expectedTiledImage)
    }

    "getTiledImageDownloader returns a function that downloads the map tile image for each TileCoordinates" {
        val tileCoordinatesList = listOf(
                TileCoordinates(1, 2, LOD16),
                TileCoordinates(2, 2, LOD16),
                TileCoordinates(1, 3, LOD16),
                TileCoordinates(2, 3, LOD16)
        )
        val expectedMapTileImages = listOf(
                createTestImage(Color.RED),
                createTestImage(Color.GREEN),
                createTestImage(Color.BLUE),
                createTestImage(Color.WHITE)
        )
        var i = 0
        val downloadMapTileImage = { _: TileCoordinates ->
            expectedMapTileImages[i++]
        }

        val downloadTiledImage = getTiledImageDownloader(downloadMapTileImage)
        val (imageList, tiledImage) = downloadTiledImage(Pair(tileCoordinatesList, aTiledImage))

        imageList should containsAll(expectedMapTileImages)
        tiledImage should beTheSameInstanceAs(aTiledImage)
    }

    "getMapTileImageCombiner returns a function that draws a list of images onto a single image" {
        val downloadedImages = listOf(createTestImage(Color.RED), createTestImage(Color.GREEN), createTestImage(Color.BLUE), createTestImage(Color.YELLOW), createTestImage(Color.WHITE))

        val combineMapTileImages = getMapTileImageCombiner()
        val (actualImage, tiledImage) = combineMapTileImages(Pair(downloadedImages, aTiledImage))

        actualImage.width shouldEqual 4096
        actualImage.height shouldEqual 4096
        actualImage.getRGB(0, 0) shouldEqual Color.RED.rgb
        actualImage.getRGB(256, 0) shouldEqual Color.GREEN.rgb
        actualImage.getRGB(512, 0) shouldEqual Color.BLUE.rgb
        actualImage.getRGB(768, 0) shouldEqual Color.YELLOW.rgb
        actualImage.getRGB(1024, 0) shouldEqual Color.WHITE.rgb
        tiledImage should beTheSameInstanceAs(aTiledImage)
    }

    "getTiledImageWriter returns a function that writes the downloaded and combined image to disk" {
        val tiledImage = TiledImage(".", TileCoordinates(1, 2, LOD16))
        val expectedImage = createTestImage(Color.RED, Color.GREEN, Color.BLUE)
        Files.deleteIfExists(Paths.get(tiledImage.fileName))

        val writeTiledImage = getTiledImageWriter(".")
        val actualTiledImage = writeTiledImage(Pair(expectedImage, tiledImage))

        val actualImage = ImageIO.read(File(tiledImage.fileName))
        actualImage.width shouldEqual expectedImage.width
        actualImage.height shouldEqual expectedImage.height
        actualImage.getRGB(0, 0) shouldEqual Color.RED.rgb
        actualImage.getRGB(1, 0) shouldEqual Color.GREEN.rgb
        actualImage.getRGB(2, 0) shouldEqual Color.BLUE.rgb
        actualTiledImage should beTheSameInstanceAs(tiledImage)
    }
})

fun createTestImage(vararg pixels: Color): BufferedImage {
    val image = BufferedImage(pixels.size, 1, BufferedImage.TYPE_INT_RGB)
    pixels.forEachIndexed { index, color -> image.setRGB(index, 0, color.rgb) }

    return image
}
