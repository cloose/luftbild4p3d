package org.luftbild4p3d.osm

import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import org.luftbild4p3d.bing.ImageryMetadata
import org.luftbild4p3d.bing.ImageryRestApi
import org.luftbild4p3d.bing.Tile
import java.util.*

class OverpassOsmApi(val httpGet: (String) -> Result<String, FuelError>) {

    companion object {
        val URLS = listOf("http://overpass-api.de/api/interpreter?data=({query});(._;>>;);out geom;", "http://overpass.osm.rambler.ru/cgi/interpreter?data=({query});(._;>>;);out geom;")
        val READ_TIMEOUT = 600000 // 600000 milliseconds = 10 minute.

        val fuelHttpGet = { url: String -> url.httpGet().timeoutRead(READ_TIMEOUT).responseString().third }
    }

    constructor() : this(fuelHttpGet)

    private fun tagQuery(tag: String, boundingBox: String) = "node[$tag]($boundingBox);way[$tag]($boundingBox);relation[$tag]($boundingBox);"

    fun getOsmData(boundingBox: BoundingBox, tags: Map<String, String>): String {
        val query = tags.map { "\"${it.key}\"" + if (it.value.isNotEmpty()) "=\"${it.value}\"" else "" }.map { tagQuery(it, boundingBox.toString()) }.joinToString("")
        val result = httpGet(URLS[Random().nextInt(URLS.size)].replace("{query}", query))

        when (result) {
            is Result.Failure -> {
                throw RuntimeException("Failed to get OSM data from Overpass API", result.getException())
            }
            is Result.Success -> {
                return result.value
            }
        }
    }
}