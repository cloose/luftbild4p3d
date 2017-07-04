package org.luftbild4p3d.bing

import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import java.util.*

class BingMapTileApi(val metadata: ImageryMetadata, val httpGet: (String) -> Result<ByteArray, FuelError>) {

    companion object {
        val fuelHttpGet = { url: String -> url.httpGet().response().third }
    }

    constructor(metadata: ImageryMetadata) : this(metadata, fuelHttpGet)

    fun downloadMapTile(tile: Tile): ByteArray {
        val url = mapTileUrl().replace("{quadkey}", tile.toQuadKey())
        val result = httpGet(url)

        when (result) {
            is Result.Success -> {
                return result.value
            }
            is Result.Failure -> {
                throw RuntimeException("Failed to download map tile from URL: $url", result.getException())
            }
        }
    }

    fun mapTileUrl(): String {
        val numberOfSubDomains = metadata.resourceSets[0].resources[0].imageUrlSubdomains.size
        val subDomain = metadata.resourceSets[0].resources[0].imageUrlSubdomains[Random().nextInt(numberOfSubDomains)]
        return metadata.resourceSets[0].resources[0].imageUrl.replace("{subdomain}", subDomain)
    }

}