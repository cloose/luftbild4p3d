package org.luftbild4p3d.bing

import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.result.Result
import io.kotlintest.matchers.*
import io.kotlintest.specs.StringSpec
import java.net.URL

class ImageryRestApiTest : StringSpec({

    "getImageryMetadata calls Bing Imagery Metadata REST API" {
        var actualUrl = ""
        val httpGet = { url: String ->
            actualUrl = url
            Result.Success<ImageryMetadata, FuelError>(ImageryMetadata("", "", "", arrayOf()))
        }

        getImageryMetadata("bingApiKey", httpGet)

        actualUrl should startWith("http://dev.virtualearth.net/REST/v1/Imagery/Metadata/Aerial?o=json&key=")
    }

    "getImageryMetadata passes Bing API key with REST call" {
        var actualUrl = ""
        val httpGet = { url: String ->
            actualUrl = url
            Result.Success<ImageryMetadata, FuelError>(ImageryMetadata("", "", "", arrayOf()))
        }

        getImageryMetadata("bingApiKey", httpGet)

        actualUrl should include("bingApiKey")
    }

    "getImageryMetadata returns received metadata on success" {
        val expected = ImageryMetadata("", "", "", arrayOf())

        val httpGetCall = { _: String ->
            Result.Success<ImageryMetadata, FuelError>(expected)
        }

        val actual = getImageryMetadata("bingApiKey", httpGetCall)

        actual shouldBe expected
    }

    "getImageryMetadata throws exception if REST call failed" {
        val httpGetCall = { _: String ->
            val error = FuelError()
            error.exception = RuntimeException()
            Result.Failure<ImageryMetadata, FuelError>(error)
        }

        val exception = shouldThrow<RuntimeException> {
            getImageryMetadata("bingApiKey", httpGetCall)
        }

        exception.message ?: "" should startWith("Failed to get metadata from Bing Imagery REST API")
    }

    "toMapTileImageUrl returns function that returns the image url for a Bing map tile" {
        val tile = Tile(1, 1, LevelOfDetail.LOD16)
        val imageUrlTemplate = "http://ecn.{subdomain}.tiles.virtualearth.net/tiles/a{quadkey}.jpeg?g=5907"
        val metadata = ImageryMetadata(resourceSets = arrayOf(ResourceSet(resources = arrayOf(Resource(imageUrl = imageUrlTemplate, imageUrlSubdomains = arrayOf("s1"))))))

        val tileToImageUrl = toMapTileImageUrl(metadata)

        tileToImageUrl(tile) shouldEqual URL("http://ecn.s1.tiles.virtualearth.net/tiles/a0000000000000003.jpeg?g=5907")
    }
})
