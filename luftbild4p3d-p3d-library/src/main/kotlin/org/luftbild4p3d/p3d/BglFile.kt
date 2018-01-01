package org.luftbild4p3d.p3d

import org.luftbild4p3d.bing.types.TileCoordinates
import org.luftbild4p3d.bing.types.getTileCoordinatesGenerator

data class BglFile(val path: String, val tileCoordinates: TileCoordinates) {

    val baseFileName = "OP_${tileCoordinates.x}_${tileCoordinates.y}"

}

fun getBglFileCreator(workFolderPath: String): (TileCoordinates) -> BglFile {
    return { tileCoordinates -> BglFile(workFolderPath, tileCoordinates) }
}

fun generateTileCoordinatesList(bglFile: BglFile): Pair<List<TileCoordinates>, BglFile> {
    val generateTileCoordinatesList = getTileCoordinatesGenerator(3, 3, 16)
    return Pair(generateTileCoordinatesList(bglFile.tileCoordinates), bglFile)
}


fun getTiledImagesForBglFileDownloader(tiledImageGenerator: (TileCoordinates) -> TiledImage) : (Pair<List<TileCoordinates>, BglFile>) -> Pair<List<TiledImage>, BglFile> {
    return { (tileCoordinateList, bglFile) -> Pair(tileCoordinateList.map(tiledImageGenerator), bglFile) }
}
