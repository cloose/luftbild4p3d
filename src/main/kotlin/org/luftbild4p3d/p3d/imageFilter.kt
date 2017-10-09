package org.luftbild4p3d.p3d

import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import java.awt.Color
import java.awt.image.BufferedImage
import kotlin.system.measureTimeMillis

fun saturatePixel(sourceRgb: Int): Int {
    val red = sourceRgb shr 16 and 0xFF
    val green = sourceRgb shr 8 and 0xFF
    val blue = sourceRgb shr 0 and 0xFF

    val hsb = Color.RGBtoHSB(red, green, blue, null)
    val saturatedColor = Math.min(hsb[1] * 1.5f, 1.0f)

    return Color.HSBtoRGB(hsb[0], saturatedColor, hsb[2])
}

fun saturateImage(sourceImage: BufferedImage): BufferedImage {
    val destinationImage = BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), sourceImage.getType())

    val benchmark = measureTimeMillis {

        val completables = (0..15).map { offset ->
            Completable.fromAction {
                (0..255).map { row ->
                    val y = row + offset * 256
                    val line = sourceImage.getRGB(0, y, sourceImage.getWidth(), 1, null, 0, sourceImage.getWidth())
                    val resultLine = line.map { saturatePixel(it) }.toIntArray()
                    destinationImage.setRGB(0, y, sourceImage.getWidth(), 1, resultLine, 0, sourceImage.getWidth())
                }
            }.subscribeOn(Schedulers.computation())
        }

        Completable.merge(completables).blockingAwait()

    }
    println("It took $benchmark milliseconds to saturate the image")

    return destinationImage
}
