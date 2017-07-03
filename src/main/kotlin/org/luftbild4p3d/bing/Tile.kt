package org.luftbild4p3d.bing

data class Tile(val x: Int, val y: Int, val levelOfDetail: LevelOfDetail) {

    companion object {
        val SIZE = 256
    }

    fun toPixelCoordinates() = PixelCoordinates(x * SIZE, y * SIZE, levelOfDetail)

    fun toQuadKey(): String {
        val quadKey = StringBuilder()

        for (i in levelOfDetail.bingLOD downTo 1) {
            var digit = '0'
            val mask = 1 shl (i - 1)

            if ((x and mask) != 0) {
                digit++
            }

            if ((y and mask) != 0) {
                digit++
                digit++
            }

            quadKey.append(digit)
        }

        return quadKey.toString()
    }

}
