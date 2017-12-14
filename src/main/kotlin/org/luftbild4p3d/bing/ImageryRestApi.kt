package org.luftbild4p3d.bing

import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import java.net.URL

fun getImageryMetadata(bingApiKey: String) = getImageryMetadata(bingApiKey, { url -> url.httpGet().responseObject(ImageryMetadata.Deserializer()).third })

fun getImageryMetadata(bingApiKey: String, httpGet: (String) -> Result<ImageryMetadata, FuelError>) : ImageryMetadata {
    val result = httpGet("http://dev.virtualearth.net/REST/v1/Imagery/Metadata/Aerial?o=json&key=$bingApiKey")

    when (result) {
        is Result.Success -> return result.value
        is Result.Failure -> throw RuntimeException("Failed to get metadata from Bing Imagery REST API", result.getException())
    }
}

fun toMapTileImageUrl(metadata: ImageryMetadata): (Tile) -> URL {
    return { tile -> URL(metadata.imageUrl().replace("{quadkey}", tile.toQuadKey())) }
}
