package org.luftbild4p3d.bing

import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.result.Result
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class BingMapTileApiTest {

    val aTile = Tile(0, 0, LevelOfDetail.LOD16)

    @Test
    fun assemblesMapTileUrlFromMetadata() {
        var actualUrl = ""
        val httpGetCall = { url: String ->
            actualUrl = url
            Result.Success<ByteArray, FuelError>(ByteArray(10))
        }

        val tile = Tile(34406, 21042, LevelOfDetail.LOD16)

        BingMapTileApi(imageryMetadata(), httpGetCall).downloadMapTile(tile)

        assertEquals("1202013001320130", tile.toQuadKey())
        assertTrue { actualUrl.contains("http://ecn.t0.tiles.virtualearth.net/tiles/a1202013001320130.jpeg?g=5777") }
    }

    @Test
    fun returnsReceivedByteArrayOnSuccess() {
        val expected = ByteArray(10)
        val httpGetCall = { _: String ->
            Result.Success<ByteArray, FuelError>(expected)
        }

        val actual = BingMapTileApi(imageryMetadata(), httpGetCall).downloadMapTile(aTile)

        assertEquals(expected, actual)
    }

    @Test
    fun throwsExceptionIfHttpCallFails() {
        val httpGetCall = { _: String ->
            val error = FuelError()
            error.exception = RuntimeException()
            Result.Failure<ByteArray, FuelError>(error)
        }

        assertFailsWith(RuntimeException::class) {
            BingMapTileApi(imageryMetadata(), httpGetCall).downloadMapTile(aTile)
        }
    }

    private fun imageryMetadata(): ImageryMetadata {
        val resource = Resource(256, 256, "http://ecn.{subdomain}.tiles.virtualearth.net/tiles/a{quadkey}.jpeg?g=5777", arrayOf("t0"), "")
        val resourceSet = ResourceSet(1, arrayOf(resource))
        return ImageryMetadata("", "", "", arrayOf(resourceSet))
    }

}