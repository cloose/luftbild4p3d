package org.luftbild4p3d.p3d

import io.kotlintest.matchers.haveSize
import io.kotlintest.matchers.should
import io.kotlintest.matchers.shouldEqual
import io.kotlintest.specs.StringSpec
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.luftbild4p3d.bing.LevelOfDetail
import org.luftbild4p3d.bing.Tile
import org.luftbild4p3d.bing.Wgs84Coordinates

class AreaTest : StringSpec({

    "topLeftTile is calculated from the passed WGS84 coordinates at passed level of details" {
        val area = Area(Wgs84Coordinates(54.0, 10.0), LevelOfDetail.LOD16)

        area.topLeftTile shouldEqual Tile(34588, 21042, LevelOfDetail.LOD16)
    }

    "bglFiles is a list of BGL files with size BGL_FILES_PER_ROW * BGL_FILES_PER_COLUMN" {
        val area = Area(Wgs84Coordinates(54.0, 10.0), LevelOfDetail.LOD16)

        area.bglFiles should haveSize(Area.BGL_FILES_PER_ROW * Area.BGL_FILES_PER_COLUMN)
    }

    "bglFiles is a list of BGL files each starting with tile derived from topLeftTile with offset BGL_FILE_TILE_OFFSET" {
        val area = Area(Wgs84Coordinates(85.0, -180.0), LevelOfDetail.LOD16)

        area.bglFiles[0].topLeftTile shouldEqual Tile(0, 107, LevelOfDetail.LOD16)
        area.bglFiles[1].topLeftTile shouldEqual Tile(48, 107, LevelOfDetail.LOD16)
        area.bglFiles[2].topLeftTile shouldEqual Tile(96, 107, LevelOfDetail.LOD16)
        area.bglFiles[3].topLeftTile shouldEqual Tile(144, 107, LevelOfDetail.LOD16)

        area.bglFiles[4].topLeftTile shouldEqual Tile(0, 155, LevelOfDetail.LOD16)
        area.bglFiles[5].topLeftTile shouldEqual Tile(48, 155, LevelOfDetail.LOD16)
        area.bglFiles[6].topLeftTile shouldEqual Tile(96, 155, LevelOfDetail.LOD16)
        area.bglFiles[7].topLeftTile shouldEqual Tile(144, 155, LevelOfDetail.LOD16)

        area.bglFiles[8].topLeftTile shouldEqual Tile(0, 203, LevelOfDetail.LOD16)
        area.bglFiles[9].topLeftTile shouldEqual Tile(48, 203, LevelOfDetail.LOD16)
        area.bglFiles[10].topLeftTile shouldEqual Tile(96, 203, LevelOfDetail.LOD16)
        area.bglFiles[11].topLeftTile shouldEqual Tile(144, 203, LevelOfDetail.LOD16)

        area.bglFiles[12].topLeftTile shouldEqual Tile(0, 251, LevelOfDetail.LOD16)
        area.bglFiles[13].topLeftTile shouldEqual Tile(48, 251, LevelOfDetail.LOD16)
        area.bglFiles[14].topLeftTile shouldEqual Tile(96, 251, LevelOfDetail.LOD16)
        area.bglFiles[15].topLeftTile shouldEqual Tile(144, 251, LevelOfDetail.LOD16)

        area.bglFiles[16].topLeftTile shouldEqual Tile(0, 299, LevelOfDetail.LOD16)
        area.bglFiles[17].topLeftTile shouldEqual Tile(48, 299, LevelOfDetail.LOD16)
        area.bglFiles[18].topLeftTile shouldEqual Tile(96, 299, LevelOfDetail.LOD16)
        area.bglFiles[19].topLeftTile shouldEqual Tile(144, 299, LevelOfDetail.LOD16)

        area.bglFiles[20].topLeftTile shouldEqual Tile(0, 347, LevelOfDetail.LOD16)
        area.bglFiles[21].topLeftTile shouldEqual Tile(48, 347, LevelOfDetail.LOD16)
        area.bglFiles[22].topLeftTile shouldEqual Tile(96, 347, LevelOfDetail.LOD16)
        area.bglFiles[23].topLeftTile shouldEqual Tile(144, 347, LevelOfDetail.LOD16)
    }

    "processArea produces the BGL files of the area " {
        val area = Area(Wgs84Coordinates(54.0, 10.0), LevelOfDetail.LOD16)
        val actualBglFiles = mutableListOf<BglFile>()
        val produceBglFile: (BglFile, Scheduler, WorkFolder) -> Unit = { bglFile, _, _ -> actualBglFiles.add(bglFile) }

        processArea(area, WorkFolder("", area), Schedulers.single(), produceBglFile)

        actualBglFiles should haveSize(area.bglFiles.size)
        actualBglFiles.forEachIndexed{ index, actual -> actual shouldEqual area.bglFiles[index] }
    }

})