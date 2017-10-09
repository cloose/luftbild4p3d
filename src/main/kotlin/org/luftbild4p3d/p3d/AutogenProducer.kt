package org.luftbild4p3d.p3d

import org.luftbild4p3d.app.Area
import org.luftbild4p3d.app.WorkFolder
import org.luftbild4p3d.bing.Tile
import org.luftbild4p3d.osm.BoundingBox
import org.luftbild4p3d.osm.OverpassOsmApi
import java.io.File

class AutogenProducer(val workFolder: WorkFolder, val log: (String) -> Unit) {

    val osmTags = mapOf("building" to "", "roof:shape" to "", "landuse" to "", "natural" to "tree")

    fun  createAutogen(startTile: Tile) {
        val osmFileName = "${startTile.x}_${startTile.y}.osm"

        val osmFile = File("${workFolder.name}/$osmFileName")
        if (!osmFile.exists()) {
            log("Download missing Osm Data $osmFileName for autogen")
            val boundingBox = BoundingBox.create(startTile, Area.IMAGES_PER_BGL_FILE_ROW_AND_COLUMN * Area.TILES_PER_IMAGE_ROW_AND_COLUMN)
            val osmData = OverpassOsmApi().getOsmData(boundingBox, osmTags)
            osmFile.writeText(osmData)
        }

        ScenProcBatchProcess.run(osmFileName, workFolder, { line -> log(line) })
    }

}