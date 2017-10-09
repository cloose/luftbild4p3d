package org.luftbild4p3d.p3d

import org.luftbild4p3d.app.*
import org.luftbild4p3d.bing.BingMapTileApi
import org.luftbild4p3d.bing.Tile
import org.luftbild4p3d.osm.BoundingBox
import org.luftbild4p3d.osm.OverpassOsmApi
import org.luftbild4p3d.osm.parseOsmData
import org.luftbild4p3d.osm.parseOsmDataFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import javax.imageio.ImageIO

class BglFileProducer(val bingMapTileApiClient: BingMapTileApi, val workFolder: WorkFolder, val log: (String) -> Unit) {

    fun createBglFile(startTile: Tile) {

        val tilesOfBglFile = generateTiles(startTile, Area.IMAGES_PER_BGL_FILE_ROW_AND_COLUMN, Area.IMAGES_PER_BGL_FILE_ROW_AND_COLUMN, Area.TILES_PER_IMAGE_ROW_AND_COLUMN)

        val bglFileName = "OP_${startTile.x}_${startTile.y}"
        if (Files.exists(Paths.get(workFolder.name, "$bglFileName.inf"))) {
            return
        }

        val infFile = InfFile("${workFolder.name}/$bglFileName.inf", Area.IMAGES_PER_BGL_FILE_ROW_AND_COLUMN * Area.IMAGES_PER_BGL_FILE_ROW_AND_COLUMN * 2)

        infFile.use {
            tilesOfBglFile.forEachIndexed { index, tile ->
                val imageName = "BI${tile.levelOfDetail.bingLOD}_${tile.x}_${tile.y}.bmp"
                val tiledImage = TiledImage(tile, Area.TILES_PER_IMAGE_ROW_AND_COLUMN)

                if (!Files.exists(Paths.get(workFolder.imageFolderPath, imageName))) {
                    log("Download missing image $imageName")
                    val bufferedImage = ImageDownloader(bingMapTileApiClient::downloadMapTile).downloadMapTilesForImage(tiledImage)
                    ImageIO.write(saturateImage(bufferedImage), "bmp", File("${workFolder.imageFolderPath}/$imageName"))
                }

                val maskName = createWaterMask(tiledImage)

                val i = index + (index + 1)
                infFile.writeSource(i, i + 1, "${workFolder.imageFolderName}", "$imageName", tiledImage)
                infFile.writeWaterMask(i + 1, "${workFolder.imageFolderName}", "$maskName", tiledImage)
            }

            infFile.writeDestination("${workFolder.sceneryFolderName}", "$bglFileName", startTile.levelOfDetail)
        }

        ResampleProcess.run(infFile, { line -> log(line) })
    }

    private fun createWaterMask(tiledImage: TiledImage): String {
        val osmDataFile = File("${workFolder.name}/BI${tiledImage.topLeftTile.levelOfDetail.bingLOD}_${tiledImage.topLeftTile.x}_${tiledImage.topLeftTile.y}_water.osm")
        if (!osmDataFile.exists()) {
            val boundingBox = BoundingBox.create(tiledImage.topLeftTile, Area.TILES_PER_IMAGE_ROW_AND_COLUMN)
            val osmXmlData = OverpassOsmApi().getOsmData(boundingBox, mapOf("water" to "", "natural" to "water", "natural" to "coastline", "waterway" to "riverbank"))
            osmDataFile.writeText(osmXmlData)
        }

        val osm = parseOsmDataFile("${workFolder.name}/BI${tiledImage.topLeftTile.levelOfDetail.bingLOD}_${tiledImage.topLeftTile.x}_${tiledImage.topLeftTile.y}_water.osm")

        val maskName = "BI${tiledImage.topLeftTile.levelOfDetail.bingLOD}_${tiledImage.topLeftTile.x}_${tiledImage.topLeftTile.y}_water.tiff"

        val waterMask = WaterMaskImage(tiledImage.topLeftTile, osm, tiledImage.size + 1000)
        waterMask.writeToFile("${workFolder.imageFolderPath}/$maskName")

        return maskName
    }
}