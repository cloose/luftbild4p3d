package org.luftbild4p3d.bing

import io.kotlintest.matchers.shouldEqual
import io.kotlintest.specs.StringSpec
import org.luftbild4p3d.bing.types.ImageryMetadata
import org.luftbild4p3d.bing.types.LevelOfDetail
import org.luftbild4p3d.bing.types.TileCoordinates
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.net.URL
import javax.imageio.ImageIO

class BingMapTileSystemTest : StringSpec({

    "getMapTileUrlGenerator returns a function that returns map tile urls for tile coordinates" {
        val tile = TileCoordinates(1, 1, LevelOfDetail.LOD16)
        val imageUrlTemplate = "http://ecn.{subdomain}.tiles.virtualearth.net/tiles/a{quadkey}.jpeg?g=5907"
        val metadata = ImageryMetadata(256, 256, imageUrlTemplate, listOf("s1"), "")

        val tileToImageUrl = getMapTileUrlGenerator(metadata)

        tileToImageUrl(tile) shouldEqual URL("http://ecn.s1.tiles.virtualearth.net/tiles/a0000000000000003.jpeg?g=5907")
    }

    "getMapTileImageDownloader returns a function that downloads map tile images for tile coordinates" {
        val imageFile = createTestImage("test.bmp", Color.RED, Color.GREEN, Color.BLUE)
        val toMapTileUrl = { _: TileCoordinates -> imageFile.toURI().toURL() }

        val downloadMapTileImage = getMapTileImageDownloader(toMapTileUrl)
        val actualImage = downloadMapTileImage(TileCoordinates(1, 1, LevelOfDetail.LOD14))

        actualImage.getRGB(0, 0) shouldEqual Color.RED.rgb
        actualImage.getRGB(1, 0) shouldEqual Color.GREEN.rgb
        actualImage.getRGB(2, 0) shouldEqual Color.BLUE.rgb

        imageFile.delete()
    }
})

fun createTestImage(fileName: String, vararg pixels: Color) : File {
    val image = BufferedImage(pixels.size, 1, BufferedImage.TYPE_INT_RGB)
    pixels.forEachIndexed { index, color -> image.setRGB(index, 0, color.rgb) }

    val file = File(fileName)
    ImageIO.write(image, "bmp", file)

    return file
}
