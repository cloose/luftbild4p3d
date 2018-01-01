package org.luftbild4p3d.p3d

import io.kotlintest.forAll
import io.kotlintest.matchers.*
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec
import org.luftbild4p3d.bing.types.LevelOfDetail.LOD16
import org.luftbild4p3d.bing.types.TileCoordinates

class BglFileTest : StringSpec({

    "baseFileName is derived from the tile coordinates" {
        val bglFile = BglFile("path", TileCoordinates(1, 2, LOD16))

        bglFile.baseFileName shouldEqual "OP_1_2"
    }

    "getBglFileCreator returns a function that creates a BglFile from a work folder path and tile coordinates" {
        val expectedTileCoordinates = TileCoordinates(1, 2, LOD16)
        val expectedWorkFolderPath = "path"

        val createBglFile = getBglFileCreator(expectedWorkFolderPath)
        val bglFile = createBglFile(expectedTileCoordinates)

        bglFile.path shouldEqual expectedWorkFolderPath
        bglFile.tileCoordinates shouldEqual expectedTileCoordinates
    }

    "generateTileCoordinatesList generates a 3x3 matrix of TileCoordinates with an offset of 16 tiles from a BglFile" {
        val expectedBglFile = BglFile("path", TileCoordinates(1, 2, LOD16))

        val (list, bglFile) = generateTileCoordinatesList(expectedBglFile)

        bglFile should beTheSameInstanceAs(expectedBglFile)
        list should haveSize(3 * 3)
        list[0] shouldEqual expectedBglFile.tileCoordinates
        list[1] shouldEqual TileCoordinates(17, 2, LOD16)
        list[2] shouldEqual TileCoordinates(33, 2, LOD16)
        list[3] shouldEqual TileCoordinates(1, 18, LOD16)
        list[4] shouldEqual TileCoordinates(17, 18, LOD16)
        list[5] shouldEqual TileCoordinates(33, 18, LOD16)
        list[6] shouldEqual TileCoordinates(1, 34, LOD16)
        list[7] shouldEqual TileCoordinates(17, 34, LOD16)
        list[8] shouldEqual TileCoordinates(33, 34, LOD16)
    }

    "getTiledImagesForBglFileDownloader returns a function that downloads all tiled images for a BGL file" {
        val expectedBglFile = BglFile("path", TileCoordinates(1, 2, LOD16))
        val expectedTileCoordinatesList = listOf(
                TileCoordinates(1, 2, LOD16),
                TileCoordinates(2, 2, LOD16),
                TileCoordinates(1, 3, LOD16),
                TileCoordinates(2, 3, LOD16)
        )
        val generateTiledImage = { tileCoordinates: TileCoordinates -> TiledImage("path", tileCoordinates) }

        val downloadTiledImages = getTiledImagesForBglFileDownloader(generateTiledImage)
        val (imageList, bglFile) = downloadTiledImages(Pair(expectedTileCoordinatesList, expectedBglFile))

        var i = 0
        forAll(imageList) { tiledImage ->
            tiledImage.imagePath shouldEqual "path"
            tiledImage.tileCoordinates shouldEqual expectedTileCoordinatesList[i++]
        }
        bglFile should beTheSameInstanceAs(expectedBglFile)
    }
})
