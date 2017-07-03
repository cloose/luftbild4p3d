package org.luftbild4p3d.bing

import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.result.Result
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ImageryRestApiTest {

    @Test
    fun passesBingApiKeyWithRestCall() {
        var actualUrl = ""
        val httpGetCall = { url: String ->
            actualUrl = url
            Result.Success<ImageryMetadata, FuelError>(ImageryMetadata("", "", "", arrayOf()))
        }

        ImageryRestApi(httpGetCall).getMetadata()

        assertTrue { actualUrl.contains(ImageryRestApi.BING_API_KEY) }
    }

    @Test
    fun returnsReceivedMetadataOnSuccess() {
        val expected = ImageryMetadata("", "", "", arrayOf())

        val httpGetCall = { _: String ->
            Result.Success<ImageryMetadata, FuelError>(expected)
        }

        val actual = ImageryRestApi(httpGetCall).getMetadata()

        assertEquals(expected, actual)
    }

    @Test
    fun throwsExceptionIfRestCallFails() {
        val httpGetCall = { _: String ->
            val error = FuelError()
            error.exception = RuntimeException()
            Result.Failure<ImageryMetadata, FuelError>(error)
        }

        assertFailsWith(RuntimeException::class) {
            ImageryRestApi(httpGetCall).getMetadata()
        }
    }

}