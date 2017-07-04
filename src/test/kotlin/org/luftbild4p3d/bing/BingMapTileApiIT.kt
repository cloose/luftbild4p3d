package org.luftbild4p3d.bing

import org.junit.Test
import kotlin.test.assertEquals

class BingMapTileApiIT {

    @Test
    fun downloadsMapTileFromBing() {
        val tile = Tile(34406, 21042, LevelOfDetail.LOD16)

        val imageData = BingMapTileApi(imageryMetadata()).downloadMapTile(tile)

        val expected = listOf<Byte>(-1, -40, -1, -32, 0, 16, 74, 70, 73, 70)
        assertEquals(expected, imageData.take(10))
    }

    private fun imageryMetadata(): ImageryMetadata {
        val resource = Resource(256, 256, "http://ecn.{subdomain}.tiles.virtualearth.net/tiles/a{quadkey}.jpeg?g=5777", arrayOf("t0"), "")
        val resourceSet = ResourceSet(1, arrayOf(resource))
        return ImageryMetadata("", "", "", arrayOf(resourceSet))
    }

}