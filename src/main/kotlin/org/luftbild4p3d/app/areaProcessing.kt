package org.luftbild4p3d.app

import io.reactivex.Completable
import org.luftbild4p3d.bing.BingMapTileApi
import org.luftbild4p3d.bing.ImageryRestApi
import org.luftbild4p3d.bing.Tile
import org.luftbild4p3d.p3d.*

fun processArea(area: Area, log: (String) -> Unit): Completable {
    log("Process area of latitude/longitude: ${area.latitude},${area.longitude}")
    val metadata = ImageryRestApi().getMetadata()
    log(metadata.copyright)
    log(metadata.resourceSets[0].resources[0].imageUrl)

    val bingMapTileApiClient = BingMapTileApi(metadata)

    val workFolder = WorkFolder(".", area.latitude, area.longitude, area.levelOfDetail)
    workFolder.setup()

    val bglFileProducer = BglFileProducer(bingMapTileApiClient, workFolder, log)
    val autogenProducer = AutogenProducer(workFolder, log)

    val startTilesOfBglFiles = generateTiles(area.startTile, Area.BGL_FILES_PER_COLUMN, Area.BGL_FILES_PER_ROW, Area.IMAGES_PER_BGL_FILE_ROW_AND_COLUMN * Area.TILES_PER_IMAGE_ROW_AND_COLUMN)

    return Completable.fromAction {
        startTilesOfBglFiles.forEach { tile ->
            bglFileProducer.createBglFile(tile)
            autogenProducer.createAutogen(tile)
        }
    }
}

fun generateTiles(startTile: Tile, maxPerColumn: Int, maxPerRow: Int, factor: Int) = (0..maxPerColumn - 1).flatMap { y -> (0..maxPerRow - 1).map { x -> Tile(startTile.x + factor * x, startTile.y + factor * y, startTile.levelOfDetail) } }