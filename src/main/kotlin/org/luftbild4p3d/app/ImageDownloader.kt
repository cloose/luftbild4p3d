package org.luftbild4p3d.app

import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.luftbild4p3d.bing.Tile
import org.luftbild4p3d.p3d.TiledImage
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO

class ImageDownloader(val downloadMapTile: (Tile) -> ByteArray, val scheduler: Scheduler) {

    constructor(downloadMapTile: (Tile) -> ByteArray) : this(downloadMapTile, Schedulers.io())

    fun downloadMapTilesForImage(image: TiledImage): BufferedImage {
        val bufferedImage = BufferedImage(image.size, image.size, BufferedImage.TYPE_3BYTE_BGR)
        val graphics = bufferedImage.createGraphics()

        val completables = (0..image.numberOfTiles - 1).map { row ->
            Completable.fromAction {
                downloadMapTilesByRow(image.leftMostTileOfRow(row), image.numberOfTiles).mapIndexed { column, data ->
                    graphics.drawImage(ImageIO.read(ByteArrayInputStream(data)), Tile.SIZE * column, Tile.SIZE * row, null)
                }
            }.subscribeOn(scheduler)
        }

        Completable.merge(completables).blockingAwait()
        return bufferedImage
    }

    private fun downloadMapTilesByRow(leftTile: Tile, numberOfTiles: Int): List<ByteArray> {
        return (0..numberOfTiles - 1).map { column ->
            val tile = Tile(leftTile.x + column, leftTile.y, leftTile.levelOfDetail)
            downloadMapTile(tile)
        }
    }

}