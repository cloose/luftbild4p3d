package org.luftbild4p3d.p3d

import org.junit.Before
import org.junit.Test
import org.luftbild4p3d.bing.LevelOfDetail
import org.luftbild4p3d.bing.Tile
import java.io.File
import kotlin.test.assertEquals

class InfFileIT {

    val filePath = "test.inf"

    @Before
    fun setup() {
        File(filePath).delete()
    }

    @Test
    fun writesMultiSourceHeaderOnInit() {
        val infFile = InfFile(filePath, 2)

        infFile.close()

        val lines = File(filePath).readLines()
        assertEquals("[Source]", lines[0])
        assertEquals("Type = MultiSource", lines[1])
        assertEquals("NumberOfSources = 2", lines[2])
    }

    @Test
    fun writesSourceForTiledImage() {
        val infFile = InfFile(filePath, 2)
        val tiledImage = TiledImage(Tile(34406, 21042, LevelOfDetail.LOD16), 16)

        infFile.writeSource(1, "sourceDirectory", "sourcefile.bmp", tiledImage)
        infFile.close()

        val lines = File(filePath).readLines()
        assertEquals("[Source1]", lines[4])
        assertEquals("Type = BMP", lines[5])
        assertEquals("Layer = Imagery", lines[6])
        assertEquals("SourceDir = \"sourceDirectory\"", lines[7])
        assertEquals("SourceFile = \"sourcefile.bmp\"", lines[8])
        assertEquals("PixelIsPoint = 0", lines[9])
        assertEquals("ulxMap = 8.997802734375", lines[10])
        assertEquals("ulyMap = 54.001311864648166", lines[11])
        assertEquals("xDim = 2.1457672119140625E-5", lines[12])
        assertEquals("yDim = 1.2619933317812096E-5", lines[13])
    }

    @Test
    fun writesDestination() {
        val infFile = InfFile(filePath, 2)

        infFile.writeDestination("destinationDirectory", "destinationfile", LevelOfDetail.LOD16)
        infFile.close()

        val lines = File(filePath).readLines()
        assertEquals("[Destination]", lines[4])
        assertEquals("DestFileType = BGL", lines[5])
        assertEquals("DestDir = \"destinationDirectory\"", lines[6])
        assertEquals("DestBaseFileName = \"destinationfile\"", lines[7])
        assertEquals("LOD = Auto,14", lines[8])
        assertEquals("CompressionQuality = 95", lines[9])
        assertEquals("UseSourceDimensions = 1", lines[10])
    }

}