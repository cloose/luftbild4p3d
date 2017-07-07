package org.luftbild4p3d.app

import io.reactivex.schedulers.Schedulers
import org.junit.Test
import org.luftbild4p3d.bing.LevelOfDetail.LOD16
import org.luftbild4p3d.bing.Tile
import kotlin.test.assertEquals

class ImageDownloaderTest {

    @Test
    fun downloadsAllMapTilesForImage() {
        var actualTiles = ArrayList<Tile>()
        val downloadMapTile = { tile: Tile ->
            actualTiles.add(tile)
            byteArrayOf()
        }

        val drawToImage: (ByteArray, Int, Int) -> Boolean = { data, x, y ->
            true
        }

        val leftTile = Tile(1, 5, LOD16)
        ImageDownloader(768, downloadMapTile, Schedulers.single()).drawMapTilesToImage(leftTile, drawToImage)

        assertEquals(listOf(Tile(1, 5, LOD16), Tile(2, 5, LOD16), Tile(3, 5, LOD16),
                Tile(1, 6, LOD16), Tile(2, 6, LOD16), Tile(3, 6, LOD16),
                Tile(1, 7, LOD16), Tile(2, 7, LOD16), Tile(3, 7, LOD16)), actualTiles)
    }

    @Test
    fun drawsEachDownloadedTileToImage() {
        val expected = listOf(byteArrayOf(1, 2, 3), byteArrayOf(4, 5, 6), byteArrayOf(7, 8, 9), byteArrayOf(10, 11, 12))
        val it = expected.listIterator()
        val downloadMapTile = { _: Tile -> it.next() }

        var actualImageData = ArrayList<ByteArray>()
        var actualX = ArrayList<Int>()
        var actualY = ArrayList<Int>()
        val drawToImage: (ByteArray, Int, Int) -> Boolean = { data, x, y ->
            actualImageData.add(data)
            actualX.add(x)
            actualY.add(y)
            true
        }

        ImageDownloader(512, downloadMapTile, Schedulers.single()).drawMapTilesToImage(Tile(1, 5, LOD16), drawToImage)

        assertEquals(listOf(0, 256, 0, 256), actualX)
        assertEquals(listOf(0, 0, 256, 256), actualY)
        assertEquals(expected, actualImageData)
    }
}