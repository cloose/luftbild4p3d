package org.luftbild4p3d.p3d

import io.kotlintest.matchers.*
import io.kotlintest.specs.StringSpec
import org.luftbild4p3d.bing.types.LevelOfDetail
import org.luftbild4p3d.bing.types.TileCoordinates
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.exp
import kotlin.streams.toList

class InfFileTest : StringSpec({

    val numberOfHeaderLines = 4
    val numberOfLinesPerImageSource = 11
    val numberOfDestinationLines = 6

    "getInfFileCreator returns a function that creates an InfFile from a list of tiled images and a BGL file" {
        val tiledImages = listOf(TiledImage("BI16", TileCoordinates(34500, 21000, LevelOfDetail.LOD16)),
                TiledImage("BI16", TileCoordinates(34516, 21000, LevelOfDetail.LOD16)))
        val bglFile = BglFile("+54+012", TileCoordinates(34500, 21000, LevelOfDetail.LOD16))

        val createInfFile = getInfFileCreator()
        val infFile = createInfFile(Pair(tiledImages, bglFile))

        infFile.filePath shouldEqual "+54+012/OP_34500_21000.inf"
        infFile.content should haveSize(numberOfHeaderLines + 2*numberOfLinesPerImageSource +  numberOfDestinationLines)
    }

    "createMultiSourceHeader returns a list of lines with number of image sources" {
        val header = createMultiSourceHeader(4)

        header shouldBe listOf("[Source]",
                "Type = MultiSource",
                "NumberOfSources = 4",
                "")
    }
    "createImageSource returns a list of lines with information for the tiled image" {
        val tiledImage = TiledImage("BI16", TileCoordinates(34500, 21000, LevelOfDetail.LOD16))

        val imageSource = createImageSource(3, tiledImage)

        imageSource shouldBe listOf("[Source4]",
                "Type = BMP",
                "Layer = Imagery",
                "SourceDir = BI16",
                "SourceFile = BI16_34500_21000.bmp",
                "PixelIsPoint = 0",
                "ulxMap = 9.51416015625",
                "ulyMap = 54.13669645687001",
                "xDim = 2.1457672119140625E-5",
                "yDim = 1.2578866206845035E-5",
                "")
    }

    "createDestination returns a list of lines with information for the resulting BGL file" {
        val bglFile = BglFile("+54+012", TileCoordinates(34500, 21000, LevelOfDetail.LOD16))

        val destination = createDestination(bglFile)

        destination shouldBe listOf("[Destination]",
                "DestFileType = BGL",
                "DestDir = scenery",
                "DestBaseFileName = OP_34500_21000",
                "LOD = Auto,14",
                "")
    }

    "saveInfFile saves content of InfFile to disk" {
        val expectedLines = listOf("line 1", "line  2")
        val infFile = InfFile("./test.inf", expectedLines)
        Files.deleteIfExists(Paths.get(infFile.filePath))

        val savedInfFile = saveInfFile(infFile)

        savedInfFile should beTheSameInstanceAs(infFile)
        Files.lines(Paths.get(infFile.filePath)).toList() shouldBe expectedLines
    }
})