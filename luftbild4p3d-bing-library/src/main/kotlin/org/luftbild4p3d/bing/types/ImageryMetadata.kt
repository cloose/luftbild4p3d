package org.luftbild4p3d.bing.types

import java.util.*

data class ImageryMetadata(val imageWidth: Int, val imageHeight: Int, val imageUrl: String, val imageUrlSubdomains: List<String>, val imageryProviders: String) : Resource()

fun nextSubDomain(metadata: ImageryMetadata) = metadata.imageUrlSubdomains[Random().nextInt(metadata.imageUrlSubdomains.size)]
