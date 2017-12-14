package org.luftbild4p3d.app

import io.reactivex.schedulers.Schedulers
import javafx.event.ActionEvent
import javafx.event.EventHandler
import org.luftbild4p3d.bing.Wgs84Coordinates
import org.luftbild4p3d.bing.downloadTile
import org.luftbild4p3d.bing.getImageryMetadata
import org.luftbild4p3d.bing.toMapTileImageUrl
import org.luftbild4p3d.p3d.*
import tornadofx.ItemViewModel
import java.time.LocalDateTime
import java.util.concurrent.Executors

class AreaViewModel : ItemViewModel<AreaModel>() {

    val latitude = bind(AreaModel::latitudeProperty)
    val longitude = bind(AreaModel::longitudeProperty)
    val levelOfDetail = bind(AreaModel::levelOfDetailProperty)
    val onGenerateTileClicked = EventHandler<ActionEvent> { _ -> generateTile() }

    init {
        item = AreaModel()
    }

    private fun generateTile() {
        this.commit()

        val area = toArea(item)
        println(area)

        val BING_API_KEY = "AnYbudcLMyPF8EIrS2Qn9PD9vNrqoAHnHzt8UwYoyJsmHHercarJNRb-YJApqpNZ"

        val metadata = getImageryMetadata(BING_API_KEY)
        println(metadata.copyright)
        println(metadata.resourceSets.first().estimatedTotal)

        val workFolder = WorkFolder(".", area)
        setupWorkFolders(workFolder)

        val threadPoolExecutor = Executors.newFixedThreadPool(4)
        val bglFileProducer = produceBglFile(downloadAllTiledImages(saveTiledImage(drawTilesOntoTiledImage(downloadTilesOfTiledImage(downloadTile(toMapTileImageUrl(metadata)))))))
        processArea(area, workFolder, Schedulers.from(threadPoolExecutor), bglFileProducer)
        threadPoolExecutor.shutdown()
        println("[${LocalDateTime.now()}]...done done")

    }

    private fun toArea(model: AreaModel) = Area(Wgs84Coordinates(model.latitude.toDouble(), model.longitude.toDouble()), model.levelOfDetail)

}