package org.luftbild4p3d.app

import org.luftbild4p3d.bing.LevelOfDetail
import java.nio.file.Files
import java.nio.file.Paths

fun Int.format(digits: Int) = String.format("%s%0${digits}d", if (this >= 0) "+" else "-", Math.abs(this))

class WorkFolder(val prefix: String, val latitude: Int, val longitude: Int, val levelOfDetail: LevelOfDetail) {

    val name = "${latitude.format(2)}${longitude.format(3)}"
    val imageFolderName = "BI${levelOfDetail.bingLOD}"
    val imageFolderPath = "$name/$imageFolderName"
    val sceneryFolderName = "scenery"
    val sceneryFolderPath = "$name/$sceneryFolderName"
    val textureFolderName = "texture"
    val textureFolderPath = "$name/$textureFolderName"

    fun setup() {
        createFolder(name)
        createFolder(imageFolderPath)
        createFolder(sceneryFolderPath)
        createFolder(textureFolderPath)
    }

    private fun createFolder(folderPath: String) {
        Files.createDirectories(Paths.get(prefix, folderPath))
    }
}