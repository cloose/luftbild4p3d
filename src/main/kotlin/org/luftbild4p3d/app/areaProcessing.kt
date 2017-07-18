package org.luftbild4p3d.app

import io.reactivex.Completable
import org.luftbild4p3d.bing.BingMapTileApi
import org.luftbild4p3d.bing.ImageryRestApi
import org.luftbild4p3d.bing.Tile
import org.luftbild4p3d.p3d.InfFile
import org.luftbild4p3d.p3d.ResampleProcess
import org.luftbild4p3d.p3d.TiledImage
import java.io.File
import javax.imageio.ImageIO

fun processArea(area: Area, log: (String) -> Unit): Completable {
    log("Process area of latitude/longitude: ${area.latitude},${area.longitude}")
    val metadata = ImageryRestApi().getMetadata()
    log(metadata.copyright)
    log(metadata.resourceSets[0].resources[0].imageUrl)

    val bingMapTileApiClient = BingMapTileApi(metadata)
    val workFolder = WorkFolder(".", area.latitude, area.longitude, area.levelOfDetail)
    workFolder.setup()

    val startTilesOfBglFiles = generateTiles(area.startTile, Area.BGL_FILES_PER_ROW_AND_COLUMN, Area.IMAGES_PER_BGL_FILE_ROW_AND_COLUMN * Area.TILES_PER_IMAGE_ROW_AND_COLUMN)

    return Completable.fromAction {
        startTilesOfBglFiles.forEach { createBglFile(it, workFolder, bingMapTileApiClient, log) }
    }
}

fun createBglFile(startTile: Tile, workFolder: WorkFolder, bingMapTileApiClient: BingMapTileApi, log: (String) -> Unit) {

    val tilesOfBglFile = generateTiles(startTile, Area.IMAGES_PER_BGL_FILE_ROW_AND_COLUMN, Area.TILES_PER_IMAGE_ROW_AND_COLUMN)

    val bglFileName = "OP_${startTile.x}_${startTile.y}"
    val infFile = InfFile("${workFolder.name}/$bglFileName.inf", Area.IMAGES_PER_BGL_FILE_ROW_AND_COLUMN * Area.IMAGES_PER_BGL_FILE_ROW_AND_COLUMN)

    infFile.use {
        tilesOfBglFile.forEachIndexed { index, tile ->
            val imageName = "BI${tile.levelOfDetail.bingLOD}_${tile.x}_${tile.y}.bmp"
            val tiledImage = TiledImage(tile, Area.TILES_PER_IMAGE_ROW_AND_COLUMN)
            val bufferedImage = ImageDownloader(bingMapTileApiClient::downloadMapTile).downloadMapTilesForImage(tiledImage)
            ImageIO.write(bufferedImage, "bmp", File("${workFolder.imageFolderPath}/$imageName"))

            infFile.writeSource(index + 1, "${workFolder.imageFolderName}", "$imageName", tiledImage)

        }

        infFile.writeDestination("${workFolder.sceneryFolderName}", "$bglFileName", startTile.levelOfDetail)
    }

    val process = ResampleProcess(workFolder)
    process.start(infFile, { line -> log(line) })
}

fun generateTiles(startTile: Tile, max: Int, factor: Int) = (0..max - 1).flatMap { y -> (0..max - 1).map { x -> Tile(startTile.x + factor * x, startTile.y + factor * y, startTile.levelOfDetail) } }