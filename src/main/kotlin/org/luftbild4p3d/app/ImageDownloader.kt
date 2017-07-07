package org.luftbild4p3d.app

import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.luftbild4p3d.bing.Tile

class ImageDownloader(imageSize: Int, val downloadMapTile: (Tile) -> ByteArray, val scheduler: Scheduler) {

    val numberOfTiles = imageSize / Tile.SIZE

    constructor(imageSize: Int, downloadMapTile: (Tile) -> ByteArray) : this(imageSize, downloadMapTile, Schedulers.io())

    fun drawMapTilesToImage(topLeftTile: Tile, drawToImage: (ByteArray, Int, Int) -> Boolean) {
        val completables = (0..numberOfTiles - 1).map { row ->
            Completable.fromAction {
                val leftTile = Tile(topLeftTile.x, topLeftTile.y + row, topLeftTile.levelOfDetail)
                downloadMapTilesByRow(leftTile).mapIndexed { column, data ->
                    drawToImage(data, Tile.SIZE * column, Tile.SIZE * row)
                }
            }.subscribeOn(scheduler)
        }

        Completable.merge(completables).blockingAwait()
    }

    private fun downloadMapTilesByRow(leftTile: Tile): List<ByteArray> {
        return (0..numberOfTiles - 1).map { column ->
            val tile = Tile(leftTile.x + column, leftTile.y, leftTile.levelOfDetail)
            downloadMapTile(tile)
        }
    }

}