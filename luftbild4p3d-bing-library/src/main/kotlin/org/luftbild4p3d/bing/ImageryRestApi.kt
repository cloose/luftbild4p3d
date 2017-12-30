package org.luftbild4p3d.bing

import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import org.luftbild4p3d.bing.types.ImageryMetadata
import org.luftbild4p3d.bing.types.Response

fun getImageryMetadata(bingApiKey: String, httpGet: (String) -> Result<Response, FuelError>) : ImageryMetadata? {
    val result = httpGet("http://dev.virtualearth.net/REST/v1/Imagery/Metadata/Aerial?o=json&key=$bingApiKey")

    when (result) {
        is Result.Success -> return result.value.resourceSets.firstOrNull()?.resources?.firstOrNull()
        is Result.Failure -> throw RuntimeException("Failed to get metadata from Bing Imagery REST API", result.getException().cause)
    }
}

fun getImageryMetadata(bingApiKey: String) = getImageryMetadata(bingApiKey, { url -> url.httpGet().responseObject<Response>().third })
