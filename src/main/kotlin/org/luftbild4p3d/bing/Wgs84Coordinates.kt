package org.luftbild4p3d.bing

fun Double.toRadians() = this * Math.PI / 180

data class Wgs84Coordinates(val latitude: Double, val longitude: Double) {

    fun toPixelCoordinates(levelOfDetail: LevelOfDetail): PixelCoordinates {
        val sinLatitude = Math.sin(latitude.toRadians())

        val x = ((longitude + 180) / 360) * 256 * Math.pow(2.0, levelOfDetail.bingLOD.toDouble())
        val y = (0.5 - Math.log((1.0 + sinLatitude) / (1.0 - sinLatitude)) / (4 * Math.PI)) * 256 * Math.pow(2.0, levelOfDetail.bingLOD.toDouble())

        return PixelCoordinates(x.toInt(), y.toInt(), levelOfDetail)
    }

}