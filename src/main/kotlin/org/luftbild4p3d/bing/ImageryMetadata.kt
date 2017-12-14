package org.luftbild4p3d.bing

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import java.util.Random

class Resource(val imageWidth: Int = 256, val imageHeight: Int = 256, val imageUrl: String = "", val imageUrlSubdomains: Array<String>, val imageryProviders: String = "")

class ResourceSet(val estimatedTotal: Int = 1, val resources: Array<Resource>)

class ImageryMetadata(val authenticationResultCode: String = "", val brandLogoUri: String = "", val copyright: String = "", val resourceSets: Array<ResourceSet>) {

    class Deserializer : ResponseDeserializable<ImageryMetadata> {
        override fun deserialize(content: String) = Gson().fromJson(content, ImageryMetadata::class.java)
    }

    fun numberOfSubdomains() = this.resourceSets.first().resources.first().imageUrlSubdomains.size

    fun nextSubDomain() = this.resourceSets.first().resources.first().imageUrlSubdomains[Random().nextInt(this.numberOfSubdomains())]

    fun imageUrl() = this.resourceSets.first().resources.first().imageUrl.replace("{subdomain}", this.nextSubDomain())

}