package org.luftbild4p3d.bing.types

import io.kotlintest.matchers.shouldEqual
import io.kotlintest.specs.StringSpec
import java.util.logging.Level

class LevelOfDetailTest : StringSpec({

    "bingLOD returns Bing level of detail as number" {
        LevelOfDetail.LOD13.bingLOD shouldEqual 13
        LevelOfDetail.LOD14.bingLOD shouldEqual 14
        LevelOfDetail.LOD15.bingLOD shouldEqual 15
        LevelOfDetail.LOD16.bingLOD shouldEqual 16
        LevelOfDetail.LOD17.bingLOD shouldEqual 17
        LevelOfDetail.LOD18.bingLOD shouldEqual 18
        LevelOfDetail.LOD19.bingLOD shouldEqual 19
    }

    "prepar3dLOD returns Prepar3D level of detail as number" {
        LevelOfDetail.LOD13.prepar3dLOD shouldEqual 11
        LevelOfDetail.LOD14.prepar3dLOD shouldEqual 12
        LevelOfDetail.LOD15.prepar3dLOD shouldEqual 13
        LevelOfDetail.LOD16.prepar3dLOD shouldEqual 14
        LevelOfDetail.LOD17.prepar3dLOD shouldEqual 15
        LevelOfDetail.LOD18.prepar3dLOD shouldEqual 16
        LevelOfDetail.LOD19.prepar3dLOD shouldEqual 17
    }

    "mapSize returns size of Bing map (256 * 2^LOD)" {
        LevelOfDetail.LOD13.mapSize shouldEqual 2097152L
        LevelOfDetail.LOD14.mapSize shouldEqual 4194304L
        LevelOfDetail.LOD15.mapSize shouldEqual 8388608L
        LevelOfDetail.LOD16.mapSize shouldEqual 16777216L
        LevelOfDetail.LOD17.mapSize shouldEqual 33554432L
        LevelOfDetail.LOD18.mapSize shouldEqual 67108864L
        LevelOfDetail.LOD19.mapSize shouldEqual 134217728L
    }

})