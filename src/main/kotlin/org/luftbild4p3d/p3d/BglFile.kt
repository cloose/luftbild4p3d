package org.luftbild4p3d.p3d

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
import org.luftbild4p3d.bing.Tile
import org.luftbild4p3d.bing.generateTiles
import org.luftbild4p3d.p3d.Area.Companion.TILES_PER_IMAGE_ROW_AND_COLUMN
import java.time.LocalDateTime


data class BglFile(val topLeftTile: Tile, private val imagesPerRowAndColumn: Int) {

    val tiledImages: List<TiledImage> = generateTiles(topLeftTile, imagesPerRowAndColumn, imagesPerRowAndColumn, TILES_PER_IMAGE_ROW_AND_COLUMN).map { TiledImage(it, TILES_PER_IMAGE_ROW_AND_COLUMN) }

}

fun downloadAllTiledImages(downloadImage: (TiledImage, WorkFolder) -> Unit): (BglFile, Scheduler, WorkFolder) -> Unit {
    return { bglFile, scheduler, workFolder ->
        bglFile.tiledImages.toObservable().flatMap {
            Observable.just(it).subscribeOn(scheduler).map{ tiledImage ->  downloadImage(tiledImage, workFolder) }
        }.blockingSubscribe()
    }
}

fun produceBglFile(downloadAllImages: (BglFile, Scheduler, WorkFolder) -> Unit): (BglFile, WorkFolder) -> Unit {
    return { bglFile, workFolder ->
        println("[${LocalDateTime.now()}] produce BGL file (${Thread.currentThread().name})")
        downloadAllImages(bglFile, Schedulers.computation(), workFolder)
        saveInfFile(createInfFile(bglFile, workFolder.sceneryFolderPath))
        println("[${LocalDateTime.now()}]...BGL file done")
    }
}