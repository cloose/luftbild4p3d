package org.luftbild4p3d.bing

import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.google.gson.Gson

data class Resource(val imageWidth: Int, val imageHeight: Int, val imageUrl: String, val imageUrlSubdomains: Array<String>, val imageryProviders: String)

data class ResourceSet(val estimatedTotal: Int, val resources: Array<Resource>)

data class ImageryMetadata(val authenticationResultCode: String, val brandLogoUri: String, val copyright: String, val resourceSets: Array<ResourceSet>) {

    class Deserializer : ResponseDeserializable<ImageryMetadata> {
        override fun deserialize(content: String) = Gson().fromJson(content, ImageryMetadata::class.java)
    }

}

class ImageryRestApi(val httpGet: (String) -> Result<ImageryMetadata, FuelError>) {

    companion object {
        val BING_API_KEY = ""
        val REST_URL = "http://dev.virtualearth.net/REST/v1/Imagery/Metadata/Aerial?o=json&key=$BING_API_KEY"

        val fuelHttpGet = { url: String -> url.httpGet().responseObject(ImageryMetadata.Deserializer()).third }
    }

    constructor() : this(fuelHttpGet)

    fun getMetadata(): ImageryMetadata {
        val result = httpGet(REST_URL)

        when (result) {
            is Result.Failure -> {
                throw RuntimeException("Failed to get metadata from Bing Imagery REST API", result.getException())
            }
            is Result.Success -> {
                return result.value
            }
        }
    }

}