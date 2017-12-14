package org.luftbild4p3d.p3d

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
import org.luftbild4p3d.bing.LevelOfDetail
import org.luftbild4p3d.bing.Wgs84Coordinates
import org.luftbild4p3d.bing.generateTiles
import java.io.File
import java.time.LocalDateTime

data class Area(val coordinates: Wgs84Coordinates, val levelOfDetail: LevelOfDetail) {

    val topLeftTile = coordinates.toPixelCoordinates(levelOfDetail).toTile()
    val bglFiles: List<BglFile> = generateTiles(topLeftTile, BGL_FILES_PER_ROW, BGL_FILES_PER_COLUMN, BGL_FILE_TILE_OFFSET).map { BglFile(it, IMAGES_PER_BGL_FILE_ROW_AND_COLUMN) }

    companion object {
        val BGL_FILES_PER_ROW = 4
        val BGL_FILES_PER_COLUMN = 6
        val IMAGES_PER_BGL_FILE_ROW_AND_COLUMN = 3
        val TILES_PER_IMAGE_ROW_AND_COLUMN = 16
        val BGL_FILE_TILE_OFFSET = IMAGES_PER_BGL_FILE_ROW_AND_COLUMN * TILES_PER_IMAGE_ROW_AND_COLUMN
    }

}

fun processArea(area: Area, workFolder: WorkFolder, scheduler: Scheduler, produceBglFile: (BglFile, WorkFolder) -> Unit) {
    println("[${LocalDateTime.now()}] Process area (${Thread.currentThread().name})")
    area.bglFiles.toObservable().flatMap {
        println("[${LocalDateTime.now()}] start process of BGL file on (${Thread.currentThread().name})")
        Observable.just(it).subscribeOn(scheduler).map { bglFile ->
            produceBglFile(bglFile, workFolder)
        }
    }.blockingSubscribe()
    println("[${LocalDateTime.now()}]...area done")
}

fun saveInfFile(infFile: InfFile) {
    File(infFile.filePath).printWriter().use { writer -> infFile.content.forEach(writer::println) }
}