package org.luftbild4p3d.p3d

import org.luftbild4p3d.bing.types.TileCoordinates
import org.luftbild4p3d.bing.types.getTileCoordinatesGenerator
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

data class TiledImage(val imagePath: String, val tileCoordinates: TileCoordinates) {

    val fileName = "BI${tileCoordinates.levelOfDetail.bingLOD}_${tileCoordinates.x}_${tileCoordinates.y}.bmp"

}

fun getTiledImageCreator(imagePath: String): (TileCoordinates) -> TiledImage {
    return { tileCoordinates ->
        println("Create tiled image for coordinates ${tileCoordinates.x}, ${tileCoordinates.y}, ${tileCoordinates.levelOfDetail}")
        TiledImage(imagePath, tileCoordinates)
    }
}

fun generateTileCoordinatesList(tiledImage: TiledImage): Pair<List<TileCoordinates>, TiledImage> {
    val generateTileCoordinatesList = getTileCoordinatesGenerator(16, 16)
    return Pair(generateTileCoordinatesList(tiledImage.tileCoordinates), tiledImage)
}

fun getTiledImageDownloader(downloadMapTileImage: (TileCoordinates) -> BufferedImage): (Pair<List<TileCoordinates>, TiledImage>) -> Pair<List<BufferedImage>, TiledImage> {
    return { (tileCoordinatesList, tiledImage) -> Pair(tileCoordinatesList.map(downloadMapTileImage), tiledImage) }
}

fun getMapTileImageCombiner(): (Pair<List<BufferedImage>, TiledImage>) -> Pair<BufferedImage, TiledImage> {
    return { (mapTileImages, tiledImage) -> Pair(combineMapTileImages(mapTileImages), tiledImage) }
}

fun getTiledImageWriter(workFolderPath: String): (Pair<BufferedImage, TiledImage>) -> TiledImage {
    return { (combinedBufferedImage, tiledImage) ->
        ImageIO.write(combinedBufferedImage, "bmp", File("$workFolderPath/${tiledImage.imagePath}/${tiledImage.fileName}"))
        tiledImage
    }
}

private fun combineMapTileImages(mapTileImages: List<BufferedImage>): BufferedImage {
    val resultImage = BufferedImage(16 * 256, 16 * 256, BufferedImage.TYPE_3BYTE_BGR)
    val graphics = resultImage.createGraphics()

    mapTileImages.forEachIndexed { index, image ->
        val x = index % 16
        val y = (index - x) / 16
        graphics.drawImage(image, 256 * x, 256 * y, null)
    }

    return resultImage
}
