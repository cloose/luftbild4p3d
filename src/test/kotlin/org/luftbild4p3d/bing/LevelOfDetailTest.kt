package org.luftbild4p3d.bing

import org.junit.Test
import kotlin.test.assertEquals

class LevelOfDetailTest {

    @Test
    fun returnsBingLevelOfDetail() {
        assertEquals(14, LevelOfDetail.LOD14.bingLOD)
        assertEquals(15, LevelOfDetail.LOD15.bingLOD)
        assertEquals(16, LevelOfDetail.LOD16.bingLOD)
        assertEquals(17, LevelOfDetail.LOD17.bingLOD)
        assertEquals(18, LevelOfDetail.LOD18.bingLOD)
    }

    @Test
    fun returnsPrepar3DLevelOfDetail() {
        assertEquals(12, LevelOfDetail.LOD14.prepar3dLOD)
        assertEquals(13, LevelOfDetail.LOD15.prepar3dLOD)
        assertEquals(14, LevelOfDetail.LOD16.prepar3dLOD)
        assertEquals(15, LevelOfDetail.LOD17.prepar3dLOD)
        assertEquals(16, LevelOfDetail.LOD18.prepar3dLOD)
    }

}