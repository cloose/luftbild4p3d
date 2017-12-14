package org.luftbild4p3d.p3d

import java.nio.file.Files
import java.nio.file.Paths

data class WorkFolder(val prefix: String, private val area: Area) {

    val path = "$prefix/${area.coordinates.latitude.format(2)}${area.coordinates.longitude.format(3)}"
    val imageFolderName = "BI${area.levelOfDetail.bingLOD}"
    val imageFolderPath = "$path/$imageFolderName"
    val sceneryFolderName = "scenery"
    val sceneryFolderPath = "$path/$sceneryFolderName"
    val textureFolderName = "texture"
    val textureFolderPath = "$path/$textureFolderName"

}

fun setupWorkFolders(workFolder: WorkFolder) {
    createFolder(workFolder.path)
    createFolder(workFolder.imageFolderPath)
    createFolder(workFolder.sceneryFolderPath)
    createFolder(workFolder.textureFolderPath)
}

fun createFolder(folderPath: String) {
    Files.createDirectories(Paths.get(folderPath))
}

fun Double.format(digits: Int) = String.format("%s%0${digits}d", if (this >= 0) "+" else "-", Math.abs(this.toInt()))
