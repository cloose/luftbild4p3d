package org.luftbild4p3d.p3d

import org.luftbild4p3d.base.composition.andThen
import org.luftbild4p3d.bing.types.TileCoordinates
import org.luftbild4p3d.bing.types.getTileCoordinatesGenerator
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

data class TiledImage(val imagePath: String, val image: BufferedImage)

fun getTiledImagePathGenerator(imagePathName: String): (TileCoordinates) -> String {
    return { tileCoordinates ->
        "$imagePathName/BI${tileCoordinates.levelOfDetail.bingLOD}_${tileCoordinates.x}_${tileCoordinates.y}.bmp"
    }
}

fun combineMapTileImages(mapTileImages: List<BufferedImage>): BufferedImage {
    val resultImage = BufferedImage(16 * 256, 16 * 256, BufferedImage.TYPE_3BYTE_BGR)
    val graphics = resultImage.createGraphics()

    mapTileImages.forEachIndexed { index, image ->
        val x = index % 16
        val y = (index - x) / 16
        graphics.drawImage(image, 256 * x, 256 * y, null)
    }

    return resultImage
}

fun getTiledImageDownloader(downloadMapTileImage: (TileCoordinates) -> BufferedImage): (TileCoordinates) -> BufferedImage {
    return getTileCoordinatesGenerator(16, 16) andThen { tiles -> tiles.map(downloadMapTileImage) } andThen ::combineMapTileImages
}

fun getTiledImageGenerator(generatePath: (TileCoordinates) -> String, downloadImage: (TileCoordinates) -> BufferedImage) : (TileCoordinates) -> TiledImage {
    return { tileCoordinates -> TiledImage(generatePath(tileCoordinates), downloadImage(tileCoordinates)) }
}

fun writeTiledImage(tiledImage: TiledImage) {
    ImageIO.write(tiledImage.image, "bmp", File(tiledImage.imagePath))
}
