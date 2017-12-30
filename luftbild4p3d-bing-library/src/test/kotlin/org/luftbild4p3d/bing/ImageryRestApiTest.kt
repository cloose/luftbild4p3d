package org.luftbild4p3d.bing

import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.result.Result
import io.kotlintest.matchers.*
import io.kotlintest.specs.StringSpec
import org.luftbild4p3d.bing.types.ImageryMetadata
import org.luftbild4p3d.bing.types.ResourceSet
import org.luftbild4p3d.bing.types.Response

class ImageryRestApiTest : StringSpec({

    val emptySuccessResponse = Result.Success<Response, FuelError>(Response("", "", "", listOf()))

    "getImageryMetadata requests Imagery Metadata from Bing REST API" {
        var actualUrl = ""
        val httpGet = { url: String ->
            actualUrl = url
            emptySuccessResponse
        }

        getImageryMetadata("bingApiKey", httpGet)

        actualUrl should startWith("http://dev.virtualearth.net/REST/v1/Imagery/Metadata/Aerial?o=json&key=")
    }

    "getImageryMetadata passes Bing API key with REST call" {
        var actualUrl = ""
        val httpGet = { url: String ->
            actualUrl = url
            emptySuccessResponse
        }

        getImageryMetadata("bingApiKey", httpGet)

        actualUrl should include("bingApiKey")
    }

    "getImageryMetadata returns received metadata on success" {
        val expected = ImageryMetadata(256, 256, "http://example.com", listOf("t1"), "")

        val httpGetCall = { _: String ->
            Result.Success<Response, FuelError>(Response("", "", "", listOf(ResourceSet(1, listOf(expected)))))
        }

        val actual = getImageryMetadata("bingApiKey", httpGetCall)

        actual shouldBe expected
    }

    "getImageryMetadata returns null if list of resources in resource set is empty" {
        val httpGetCall = { _: String ->
            Result.Success<Response, FuelError>(Response("", "", "", listOf(ResourceSet(0, listOf()))))
        }

        val actual = getImageryMetadata("bingApiKey", httpGetCall)

        actual shouldBe null
    }

    "getImageryMetadata returns null if list of resource sets is empty" {
        val httpGetCall = { _: String ->
            Result.Success<Response, FuelError>(Response("", "", "", listOf()))
        }

        val actual = getImageryMetadata("bingApiKey", httpGetCall)

        actual shouldBe null
    }

    "getImageryMetadata throws exception if REST call failed" {
        val expected = RuntimeException("cause")
        val httpGetCall = { _: String -> Result.Failure<Response, FuelError>(FuelError(expected)) }

        val exception = shouldThrow<RuntimeException> {
            getImageryMetadata("bingApiKey", httpGetCall)
        }

        exception.message!! should startWith("Failed to get metadata from Bing Imagery REST API")
        exception.cause shouldBe beTheSameInstanceAs(expected)
    }
})