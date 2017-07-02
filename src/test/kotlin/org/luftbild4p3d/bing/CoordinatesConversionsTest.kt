package org.luftbild4p3d.bing

import org.junit.Test
import kotlin.test.assertEquals

class CoordinatesConversionsTest {

    @Test
    fun convertsAngleInDegreesToRadians() {
        assertEquals(0.0, 0.0.toRadians())
        assertEquals(Math.PI / 2, 90.0.toRadians())
        assertEquals(Math.PI, 180.0.toRadians())
        assertEquals(Math.PI * 3 / 2, 270.0.toRadians())
        assertEquals(Math.PI * 2, 360.0.toRadians())
    }

}