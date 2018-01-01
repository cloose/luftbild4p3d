package org.luftbild4p3d.p3d

import io.kotlintest.matchers.*
import io.kotlintest.specs.StringSpec
import org.luftbild4p3d.bing.types.LevelOfDetail.LOD16
import org.luftbild4p3d.bing.types.TileCoordinates
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import javax.imageio.ImageIO

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
})
