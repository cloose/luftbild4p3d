package org.luftbild4p3d.osm

import org.junit.Test
import org.luftbild4p3d.bing.LevelOfDetail
import org.luftbild4p3d.bing.Tile
import org.luftbild4p3d.bing.Wgs84Coordinates
import org.luftbild4p3d.bing.toRadians
import kotlin.test.assertEquals

class BoundingBoxTest {

    @Test
    fun calculatesStartCoordinates() {
        val bbox = BoundingBox.create(Tile(34406, 21042, LevelOfDetail.LOD16), 16)

        assertEquals(53.94962061777841, bbox.startLatitude)
        assertEquals(8.997802734375, bbox.startLongitude)
    }

    @Test
    fun calculatesEndCoordinates() {
        val bbox = BoundingBox.create(Tile(34406, 21042, LevelOfDetail.LOD16), 16)

        assertEquals(54.001311864648166, bbox.endLatitude)
        assertEquals(9.085693359375, bbox.endLongitude)
    }

    @Test
    fun convertsCoordinatesToCommaSeparatedString() {
        val bbox = BoundingBox.create(Tile(34406, 21042, LevelOfDetail.LOD16), 16)

        assertEquals("53.94962061777841,8.997802734375,54.001311864648166,9.085693359375", bbox.toString())
    }

}