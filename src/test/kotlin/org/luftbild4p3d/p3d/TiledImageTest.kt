package org.luftbild4p3d.p3d

import io.kotlintest.TestCaseContext
import io.kotlintest.matchers.beTheSameInstanceAs
import io.kotlintest.matchers.haveSize
import io.kotlintest.matchers.should
import io.kotlintest.matchers.shouldEqual
import io.kotlintest.specs.StringSpec
import org.luftbild4p3d.bing.LevelOfDetail
import org.luftbild4p3d.bing.Tile
import org.luftbild4p3d.bing.Wgs84Coordinates
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import javax.imageio.ImageIO

class TiledImageTest : StringSpec({

    val aWorkFolder = WorkFolder("test", Area(Wgs84Coordinates(54.0, 9.0), LevelOfDetail.LOD16))

    "fileName is derived from top left tile position and LOD" {
        val tiledImage = TiledImage(Tile(1, 2, LevelOfDetail.LOD16), 16)

        tiledImage.fileName shouldEqual "BI16_1_2.bmp"
    }

    "tiles is a list of tiles with size tilesPerRowAndColumn * tilesPerRowAndColumn" {
        val tiledImage = TiledImage(Tile(1, 2, LevelOfDetail.LOD16), 16)

        tiledImage.tiles should haveSize(16 * 16)
    }

    "tiles is a list of tiles each derived from topLeftTile" {
        val tiledImage = TiledImage(Tile(1, 2, LevelOfDetail.LOD16), 2)

        tiledImage.tiles[0] shouldEqual Tile(1, 2, LevelOfDetail.LOD16)
        tiledImage.tiles[1] shouldEqual Tile(2, 2, LevelOfDetail.LOD16)
        tiledImage.tiles[2] shouldEqual Tile(1, 3, LevelOfDetail.LOD16)
        tiledImage.tiles[3] shouldEqual Tile(2, 3, LevelOfDetail.LOD16)
    }

    "downloadTilesOfTiledImage returns a function that downloads all tiles of a tiled image" {
        val tiledImage = TiledImage(Tile(1, 2, LevelOfDetail.LOD16), 2)

        val actualTiles = mutableListOf<Tile>()
        val downloadTile = { tile: Tile ->
            actualTiles.add(tile)
            BufferedImage(2, 1, BufferedImage.TYPE_INT_RGB)
        }

        downloadTilesOfTiledImage(downloadTile)(tiledImage)

        actualTiles should haveSize(tiledImage.tiles.size)
        actualTiles[0] shouldEqual tiledImage.tiles[0]
        actualTiles[1] shouldEqual tiledImage.tiles[1]
        actualTiles[2] shouldEqual tiledImage.tiles[2]
        actualTiles[3] shouldEqual tiledImage.tiles[3]
    }

    "downloadTilesOfTiledImage returns a function that returns the downloaded tiles as list of BufferedImages" {
        val tiledImage = TiledImage(Tile(1, 2, LevelOfDetail.LOD16), 2)
        val expectedImages = listOf(
                BufferedImage(2, 1, BufferedImage.TYPE_INT_RGB),
                BufferedImage(3, 2, BufferedImage.TYPE_INT_RGB),
                BufferedImage(4, 3, BufferedImage.TYPE_INT_RGB),
                BufferedImage(5, 4, BufferedImage.TYPE_INT_RGB))
        var index = 0

        val downloadTile = { _: Tile -> expectedImages[index++] }

        val actualImages = downloadTilesOfTiledImage(downloadTile)(tiledImage)

        actualImages should haveSize(tiledImage.tiles.size)
        actualImages[0] should beTheSameInstanceAs(expectedImages[0])
        actualImages[1] should beTheSameInstanceAs(expectedImages[1])
        actualImages[2] should beTheSameInstanceAs(expectedImages[2])
        actualImages[3] should beTheSameInstanceAs(expectedImages[3])
    }

    "drawTilesOntoTiledImage returns a function that performs the download of the tiled image" {
        val expectedTiledImage = TiledImage(Tile(1, 2, LevelOfDetail.LOD16), 2)
        var actualTiledImage: TiledImage? = null
        val downloadTiles = { tiledImage: TiledImage ->
            actualTiledImage = tiledImage
            listOf(createTestImage(Color.RED))
        }

        drawTilesOntoTiledImage(downloadTiles)(expectedTiledImage)

        actualTiledImage shouldEqual expectedTiledImage
    }

    "drawTilesOntoTiledImage returns a function that draws each downloaded tile onto the tiled image" {
        val tiledImage = TiledImage(Tile(1, 2, LevelOfDetail.LOD16), 2)
        val downloadTiles = { _: TiledImage ->
            listOf(createTestImage(Color.RED), createTestImage(Color.GREEN), createTestImage(Color.BLUE), createTestImage(Color.YELLOW))
        }

        val actualImage = drawTilesOntoTiledImage(downloadTiles)(tiledImage)

        actualImage.width shouldEqual 4096
        actualImage.height shouldEqual 4096
        actualImage.getRGB(0, 0) shouldEqual Color.RED.rgb
        actualImage.getRGB(256, 0) shouldEqual Color.GREEN.rgb
        actualImage.getRGB(0, 256) shouldEqual Color.BLUE.rgb
        actualImage.getRGB(256, 256) shouldEqual Color.YELLOW.rgb
    }

    "saveTiledImage returns a function that performs the drawing of the tiled image" {
        val expectedTiledImage = TiledImage(Tile(1, 2, LevelOfDetail.LOD16), 2)
        var actualTiledImage: TiledImage? = null
        val drawTiledImage = { tiledImage: TiledImage ->
            actualTiledImage = tiledImage
            createTestImage(Color.RED)
        }

        saveTiledImage(drawTiledImage)(expectedTiledImage, aWorkFolder)

        actualTiledImage shouldEqual expectedTiledImage
    }

    "saveTiledImage returns a function that saves the drawn tiled image as BMP file" {
        val tiledImage = TiledImage(Tile(1, 2, LevelOfDetail.LOD16), 2)
        val drawTiledImage = { _: TiledImage -> createTestImage(Color.RED) }
        val imageFile = File("${aWorkFolder.imageFolderPath}/${tiledImage.fileName}")

        saveTiledImage(drawTiledImage)(tiledImage, aWorkFolder)
        val actualImage = ImageIO.read(imageFile)

        actualImage.width shouldEqual 1
        actualImage.height shouldEqual 1
        actualImage.getRGB(0, 0) shouldEqual Color.RED.rgb
        imageFile.delete()
    }
}) {
    override fun interceptTestCase(context: TestCaseContext, test: () -> Unit) {
        File("test").deleteRecursively()
        Files.createDirectories(Paths.get("test/+54+009/BI16"))

        test()
    }
}


fun createTestImage(pixel: Color): BufferedImage {
    val image = BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)
    image.setRGB(0, 0, pixel.rgb)
    return image
}