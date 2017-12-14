package org.luftbild4p3d.p3d

import io.kotlintest.matchers.shouldEqual
import io.kotlintest.specs.StringSpec
import org.luftbild4p3d.bing.LevelOfDetail
import org.luftbild4p3d.bing.Tile


class InfFileTest : StringSpec({

    "createInfFile returns an InfFile with a multi source header section" {
        val bglFile = BglFile(Tile(1, 2, LevelOfDetail.LOD16), 2)

        val actual = createInfFile(bglFile, "scenery")

        actual.section("[Source]", 4) shouldEqual listOf("[Source]", "Type = MultiSource", "NumberOfSources = 4", "")
    }

    "createInfFile returns an InfFile with an image source section for each tiled image" {
        val bglFile = BglFile(Tile(1, 2, LevelOfDetail.LOD16), 2)

        val actual = createInfFile(bglFile, "scenery")

        actual.section("[Source1]", 8) shouldEqual listOf("[Source1]", "Type = BMP", "Layer = Imagery", "SourceFile = BI16_1_2.bmp", "PixelIsPoint = 0", "ulxMap = -179.9945068359375", "ulyMap = 85.05018093458115", "")
        actual.section("[Source2]", 8) shouldEqual listOf("[Source2]", "Type = BMP", "Layer = Imagery", "SourceFile = BI16_17_2.bmp", "PixelIsPoint = 0", "ulxMap = -179.9066162109375", "ulyMap = 85.05018093458115", "")
        actual.section("[Source3]", 8) shouldEqual listOf("[Source3]", "Type = BMP", "Layer = Imagery", "SourceFile = BI16_1_18.bmp", "PixelIsPoint = 0", "ulxMap = -179.9945068359375", "ulyMap = 85.04259165077696", "")
        actual.section("[Source4]", 8) shouldEqual listOf("[Source4]", "Type = BMP", "Layer = Imagery", "SourceFile = BI16_17_18.bmp", "PixelIsPoint = 0", "ulxMap = -179.9066162109375", "ulyMap = 85.04259165077696", "")
    }

    "createInfFile returns an InfFile with a destination section" {
        val bglFile = BglFile(Tile(1, 2, LevelOfDetail.LOD16), 2)

        val actual = createInfFile(bglFile, "scenery")

        actual.section("[Destination]", 4) shouldEqual listOf("[Destination]", "DestFileType = BGL", "DestBaseFileName = OP_1_2", "")
    }

})

fun InfFile.section(sectionName: String, numberOfLines: Int) = this.content.subList(this.content.indexOf(sectionName), this.content.indexOf(sectionName) + numberOfLines)