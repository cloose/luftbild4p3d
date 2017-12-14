package org.luftbild4p3d.p3d

import io.kotlintest.matchers.haveSize
import io.kotlintest.matchers.should
import io.kotlintest.matchers.shouldEqual
import io.kotlintest.specs.StringSpec
import io.reactivex.schedulers.Schedulers
import org.luftbild4p3d.bing.LevelOfDetail
import org.luftbild4p3d.bing.Tile
import org.luftbild4p3d.bing.Wgs84Coordinates

class BglFileTest : StringSpec({

    val aWorkFolder = WorkFolder("", Area(Wgs84Coordinates(54.0, 9.0), LevelOfDetail.LOD16))

    "tiledImages is a list of tiled images with size imagesPerRowAndColumn * imagesPerRowAndColumn" {
        val bglFile = BglFile(Tile(1, 2, LevelOfDetail.LOD16), 3)

        bglFile.tiledImages should haveSize(3 * 3)
    }

    "tiledImages is a list of tiled images each starting with tile derived from topLeftTile with offset based on TILES_PER_IMAGE_ROW_AND_COLUMN" {
        val bglFile = BglFile(Tile(1, 2, LevelOfDetail.LOD16), 2)

        bglFile.tiledImages[0].topLeftTile shouldEqual Tile(1, 2, LevelOfDetail.LOD16)
        bglFile.tiledImages[1].topLeftTile shouldEqual Tile(17, 2, LevelOfDetail.LOD16)
        bglFile.tiledImages[2].topLeftTile shouldEqual Tile(1, 18, LevelOfDetail.LOD16)
        bglFile.tiledImages[3].topLeftTile shouldEqual Tile(17, 18, LevelOfDetail.LOD16)
    }

    "produceBglFile returns a function that downloads each tiled image" {
        val bglFile = BglFile(Tile(1, 2, LevelOfDetail.LOD16), 2)
        val actualTiledImages = mutableListOf<TiledImage>()
        val downloadImage: (TiledImage, WorkFolder) -> Unit = { tiledImage, _ -> actualTiledImages.add(tiledImage) }

        produceBglFile(downloadImage)(bglFile, Schedulers.single(), aWorkFolder)

        actualTiledImages should haveSize(bglFile.tiledImages.size)
        actualTiledImages[0] shouldEqual bglFile.tiledImages[0]
        actualTiledImages[1] shouldEqual bglFile.tiledImages[1]
        actualTiledImages[2] shouldEqual bglFile.tiledImages[2]
        actualTiledImages[3] shouldEqual bglFile.tiledImages[3]
    }

})
