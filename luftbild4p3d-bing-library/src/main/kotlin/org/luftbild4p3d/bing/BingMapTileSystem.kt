package org.luftbild4p3d.bing

import org.luftbild4p3d.base.composition.andThen
import org.luftbild4p3d.bing.types.ImageryMetadata
import org.luftbild4p3d.bing.types.TileCoordinates
import org.luftbild4p3d.bing.types.nextSubDomain
import java.awt.image.BufferedImage
import java.net.URL
import javax.imageio.ImageIO

fun getMapTileUrlGenerator(metadata: ImageryMetadata): (TileCoordinates) -> URL {
    return { tileCoordinates ->
        val imageUrl = metadata.imageUrl.replace("{subdomain}", nextSubDomain(metadata)).replace("{quadkey}", tileCoordinates.toQuadKey())
        URL(imageUrl)
    }
}

fun getMapTileImageDownloader(metadata: ImageryMetadata): (TileCoordinates) -> BufferedImage {
    return getMapTileUrlGenerator(metadata) andThen { url: URL -> ImageIO.read(url) }
}
