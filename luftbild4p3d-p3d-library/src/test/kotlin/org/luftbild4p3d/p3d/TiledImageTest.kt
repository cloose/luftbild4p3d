package org.luftbild4p3d.p3d

import io.kotlintest.matchers.beTheSameInstanceAs
import io.kotlintest.matchers.haveSize
import io.kotlintest.matchers.should
import io.kotlintest.matchers.shouldEqual
import io.kotlintest.specs.StringSpec
import org.luftbild4p3d.bing.types.LevelOfDetail
import org.luftbild4p3d.bing.types.TileCoordinates
import java.awt.Color
import java.awt.image.BufferedImage

class TiledImageTest : StringSpec({

    val aTestImage = createTestImage(Color.WHITE)

    "getTiledImagePathGenerator returns a function that generates a path from an image path name and tile coordinates" {
        val tileCoordinates = TileCoordinates(1, 2, LevelOfDetail.LOD16)

        val generateTiledImagePath = getTiledImagePathGenerator("path")
        val tiledImagePath = generateTiledImagePath(tileCoordinates)

        tiledImagePath shouldEqual "path/BI16_1_2.bmp"
    }

    "combineMapTileImages draws a list of images onto a single image" {
        val downloadedImages = listOf(createTestImage(Color.RED), createTestImage(Color.GREEN), createTestImage(Color.BLUE), createTestImage(Color.YELLOW), createTestImage(Color.WHITE))

        val actualImage = combineMapTileImages(downloadedImages)

        actualImage.width shouldEqual 4096
        actualImage.height shouldEqual 4096
        actualImage.getRGB(0, 0) shouldEqual Color.RED.rgb
        actualImage.getRGB(256, 0) shouldEqual Color.GREEN.rgb
        actualImage.getRGB(512, 0) shouldEqual Color.BLUE.rgb
        actualImage.getRGB(768, 0) shouldEqual Color.YELLOW.rgb
        actualImage.getRGB(1024, 0) shouldEqual Color.WHITE.rgb
    }

    "getTiledImageDownloader returns a function that generates a 16x16 list of tile coordinates and downloads the corresponding map tile image" {
        var actualTileCoordinates = mutableListOf<TileCoordinates>()
        val downloadMapTileImage = { tile: TileCoordinates ->
            actualTileCoordinates.add(tile)
            aTestImage
        }

        val downloadTiledImage = getTiledImageDownloader(downloadMapTileImage)
        downloadTiledImage(TileCoordinates(1, 2, LevelOfDetail.LOD16))

        actualTileCoordinates should haveSize(16 * 16)
    }

    "getTiledImageDownloader returns a function that combines all downloaded map tile images into a single image" {
        val expectedColors = createColorList(16 * 16)
        val images = expectedColors.map { createTestImage(it) }.listIterator()
        val downloadMapTileImage = { _: TileCoordinates -> images.next() }

        val downloadTiledImage = getTiledImageDownloader(downloadMapTileImage)
        val actualImage = downloadTiledImage(TileCoordinates(1, 2, LevelOfDetail.LOD16))

        actualImage.width shouldEqual 4096
        actualImage.height shouldEqual 4096
        var i = 0
        for (y in 0..4095 step 256) {
            for (x in 0..4095 step 256) {
                actualImage.getRGB(x, y) shouldEqual expectedColors[i++].rgb
            }
        }
    }

    "getTiledImageGenerator returns a function that generates a path and downloads an image for tile coordinates and returns them as tiled image" {
        val generatePath = { _: TileCoordinates -> "path/BI16_1_2.bmp"}
        val downloadImage = { _: TileCoordinates -> aTestImage }

        val generateTiledImage = getTiledImageGenerator(generatePath, downloadImage)
        val tiledImage = generateTiledImage(TileCoordinates(1, 2, LevelOfDetail.LOD16))

        tiledImage.imagePath shouldEqual "path/BI16_1_2.bmp"
        tiledImage.image should beTheSameInstanceAs(aTestImage)
    }

})

fun createTestImage(vararg pixels: Color): BufferedImage {
    val image = BufferedImage(pixels.size, 1, BufferedImage.TYPE_INT_RGB)
    pixels.forEachIndexed { index, color -> image.setRGB(index, 0, color.rgb) }

    return image
}

fun createColorList(count: Int): List<Color> {
    return (0 until count).map { Color(it, 255, 255) }
}
