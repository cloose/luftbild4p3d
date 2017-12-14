package org.luftbild4p3d.p3d

import org.luftbild4p3d.bing.Tile

data class InfFile(val filePath: String, val content: List<String>)

fun createInfFile(bglFile: BglFile, sceneryFolderPath: String): InfFile {
    val header = createMultiSourceHeader(bglFile.tiledImages.size)
    val imageSources = bglFile.tiledImages.mapIndexed(::createImageSource).flatten()
    val destination = createDestination(bglFile.topLeftTile)
    return InfFile("$sceneryFolderPath/OP_${bglFile.topLeftTile.x}_${bglFile.topLeftTile.y}.inf", header + imageSources + destination)
}

fun createMultiSourceHeader(sourceCount: Int): List<String> {
    return listOf("[Source]",
            "Type = MultiSource",
            "NumberOfSources = $sourceCount",
            "")
}

fun createImageSource(index: Int, tiledImage: TiledImage): List<String> {
    val coordinates = tiledImage.topLeftTile.toPixelCoordinates().toWgs84Coordinates()
    return listOf("[Source${index+1}]",
            "Type = BMP",
            "Layer = Imagery",
            "SourceFile = ${tiledImage.fileName}",
            "PixelIsPoint = 0",
            "ulxMap = ${coordinates.longitude}",
            "ulyMap = ${coordinates.latitude}",
            "")
}

fun createDestination(topLeftTile: Tile): List<String> {
    return listOf("[Destination]",
            "DestFileType = BGL",
            "DestBaseFileName = OP_${topLeftTile.x}_${topLeftTile.y}",
            "")
}
