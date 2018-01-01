package org.luftbild4p3d.p3d

import org.luftbild4p3d.bing.types.TileCoordinates
import java.io.File

data class InfFile(val filePath: String, val content: List<String>)

fun getInfFileCreator(): (Pair<List<TiledImage>, BglFile>) -> InfFile {
    return { (tiledImageList, bglFile) ->
        val header = createMultiSourceHeader(tiledImageList.size)
        val imageSources = tiledImageList.mapIndexed(::createImageSource).flatten()
        val destination = createDestination(bglFile)
        InfFile("${bglFile.path}/${bglFile.baseFileName}.inf", header + imageSources + destination)
    }
}

fun createMultiSourceHeader(sourceCount: Int): List<String> {
    return listOf("[Source]",
            "Type = MultiSource",
            "NumberOfSources = $sourceCount",
            "")
}

fun createImageSource(index: Int, tiledImage: TiledImage): List<String> {
    val coordinates = tiledImage.tileCoordinates.toPixelCoordinates().toWgs84Coordinates()

    val (pixelSizeX, pixelSizeY) = pixelSizes(tiledImage.tileCoordinates)
    return listOf("[Source${index+1}]",
            "Type = BMP",
            "Layer = Imagery",
            "SourceDir = ${tiledImage.imagePath}",
            "SourceFile = ${tiledImage.fileName}",
            "PixelIsPoint = 0",
            "ulxMap = ${coordinates.longitude}",
            "ulyMap = ${coordinates.latitude}",
            "xDim = $pixelSizeX",
            "yDim = $pixelSizeY",
            "")
}

fun createDestination(bglFile: BglFile): List<String> {
    return listOf("[Destination]",
            "DestFileType = BGL",
            "DestDir = scenery",
            "DestBaseFileName = ${bglFile.baseFileName}",
            "LOD = Auto,${bglFile.tileCoordinates.levelOfDetail.prepar3dLOD}",
            "")
}

fun saveInfFile(infFile: InfFile) : InfFile {
    File(infFile.filePath).printWriter().use { writer -> infFile.content.forEach(writer::println) }
    return infFile
}

fun pixelSizes(tileCoordinates: TileCoordinates) : Pair<Double, Double> {
    val numberOfPixelsInTiledImage = 256 * 16
    val wgs84Coordinates = tileCoordinates.toPixelCoordinates().toWgs84Coordinates()

    val longitudeOfRightTile = TileCoordinates(tileCoordinates.x + 16, tileCoordinates.y, tileCoordinates.levelOfDetail).toPixelCoordinates().toWgs84Coordinates().longitude
    val pixelSizeX = Math.abs(longitudeOfRightTile - wgs84Coordinates.longitude) / numberOfPixelsInTiledImage

    val latitudeOfBelowTile = TileCoordinates(tileCoordinates.x, tileCoordinates.y + 16, tileCoordinates.levelOfDetail).toPixelCoordinates().toWgs84Coordinates().latitude
    val pixelSizeY = Math.abs(latitudeOfBelowTile - wgs84Coordinates.latitude) / numberOfPixelsInTiledImage

    return Pair(pixelSizeX, pixelSizeY)
}
