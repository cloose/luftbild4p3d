package org.luftbild4p3d.p3d

import org.luftbild4p3d.bing.Tile
import org.luftbild4p3d.bing.generateTiles
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

data class TiledImage(val topLeftTile: Tile, val tilesPerRowAndColumn: Int) {

    val fileName: String = "BI${topLeftTile.levelOfDetail.bingLOD}_${topLeftTile.x}_${topLeftTile.y}.bmp"
    val tiles: List<Tile> = generateTiles(topLeftTile, tilesPerRowAndColumn, tilesPerRowAndColumn, 1)

}

fun downloadTilesOfTiledImage(downloadTile: (Tile) -> BufferedImage): (TiledImage) -> List<BufferedImage> {
    return { tiledImage ->
        println("download all tiles of tiled image ${tiledImage.topLeftTile.x}_${tiledImage.topLeftTile.y} (${Thread.currentThread().name})")
        tiledImage.tiles.map(downloadTile)
    }
}

fun drawTilesOntoTiledImage(downloadTilesOfTiledImage: (TiledImage) -> List<BufferedImage>): (TiledImage) -> BufferedImage {
    return { tiledImage ->
        val resultImage = BufferedImage(4096, 4096, BufferedImage.TYPE_3BYTE_BGR)
        val graphics = resultImage.createGraphics()

        downloadTilesOfTiledImage(tiledImage).forEachIndexed { index, image ->
            val x = index % tiledImage.tilesPerRowAndColumn
            val y = (index - x) / tiledImage.tilesPerRowAndColumn
            graphics.drawImage(image, Tile.SIZE * x, Tile.SIZE * y, null)
        }

        resultImage
    }
}

fun saveTiledImage(drawTiledImage: (TiledImage) -> BufferedImage): (TiledImage, WorkFolder) -> Unit {
    return { tiledImage, workFolder -> ImageIO.write(drawTiledImage(tiledImage), "bmp", File("${workFolder.imageFolderPath}/${tiledImage.fileName}")) }
}