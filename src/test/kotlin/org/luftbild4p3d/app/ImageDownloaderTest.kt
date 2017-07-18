package org.luftbild4p3d.app

import io.reactivex.schedulers.Schedulers
import org.junit.Test
import org.luftbild4p3d.bing.LevelOfDetail.LOD16
import org.luftbild4p3d.bing.Tile
import org.luftbild4p3d.p3d.TiledImage
import java.awt.image.BufferedImage
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

        val tiledImage = TiledImage(Tile(1, 5, LOD16), 3)
        ImageDownloader(downloadMapTile, Schedulers.single()).downloadMapTilesForImage(tiledImage)

        assertEquals(listOf(Tile(1, 5, LOD16), Tile(2, 5, LOD16), Tile(3, 5, LOD16),
                Tile(1, 6, LOD16), Tile(2, 6, LOD16), Tile(3, 6, LOD16),
                Tile(1, 7, LOD16), Tile(2, 7, LOD16), Tile(3, 7, LOD16)), actualTiles)
    }

    @Test
    fun returnsDownloadedBufferedImage() {
        val tiledImage = TiledImage(Tile(1, 5, LOD16), 2)
        val expected = listOf(byteArrayOf(1, 2, 3), byteArrayOf(4, 5, 6), byteArrayOf(7, 8, 9), byteArrayOf(10, 11, 12))
        val it = expected.listIterator()
        val downloadMapTile = { _: Tile -> it.next() }

        val bufferedImage = ImageDownloader(downloadMapTile, Schedulers.single()).downloadMapTilesForImage(tiledImage)

        assertEquals(BufferedImage.TYPE_3BYTE_BGR, bufferedImage.type)
        assertEquals(512, bufferedImage.width)
        assertEquals(512, bufferedImage.height)
    }
}