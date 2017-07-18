package org.luftbild4p3d.p3d

import org.luftbild4p3d.bing.LevelOfDetail
import java.io.Closeable
import java.io.File

class InfFile(val filePath: String, val numberOfSources: Int) : Closeable {

    private val writer = File(filePath).printWriter()

    init {
        writeMultiSourceHeader()
    }

    fun writeSource(index: Int, sourceDirectory: String, sourceFile: String, tiledImage: TiledImage) {
        writer.appendln("[Source$index]")
        writer.appendln("Type = BMP")
        writer.appendln("Layer = Imagery")
        writer.appendln("SourceDir = \"$sourceDirectory\"")
        writer.appendln("SourceFile = \"$sourceFile\"")
        writer.appendln("PixelIsPoint = 0")
        writer.appendln("ulxMap = ${tiledImage.longitude}")
        writer.appendln("ulyMap = ${tiledImage.latitude}")
        writer.appendln("xDim = ${tiledImage.pixelSizeX()}")
        writer.appendln("yDim = ${tiledImage.pixelSizeY()}")
        //writer.appendln("Variation = March,April,May,June,July,August,September,October,November")
        //writer.appendln("NullValue = ,,,,0")
        //writer.appendln("Channel_LandWaterMask = ${waterMask}.0")
        writer.appendln("")
    }

    fun writeDestination(destinationDirectory: String, destinationFile: String, levelOfDetail: LevelOfDetail) {
        writer.appendln("[Destination]")
        writer.appendln("DestFileType = BGL")
        writer.appendln("DestDir = \"$destinationDirectory\"")
        writer.appendln("DestBaseFileName = \"$destinationFile\"")
        writer.appendln("LOD = Auto,${levelOfDetail.prepar3dLOD}")
        writer.appendln("CompressionQuality = 95")
        writer.appendln("UseSourceDimensions = 1")
        writer.appendln("")

    }

    override fun close() {
        writer.close()
    }

    private fun writeMultiSourceHeader() {
        writer.appendln("[Source]")
        writer.appendln("Type = MultiSource")
        writer.appendln("NumberOfSources = $numberOfSources")
        writer.appendln()
    }
}