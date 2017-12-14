package org.luftbild4p3d.bing

import io.kotlintest.matchers.shouldEqual
import io.kotlintest.specs.StringSpec

class LevelOfDetailTest : StringSpec({

    "bingLOD returns Bing level of detail as number" {
        LevelOfDetail.LOD14.bingLOD shouldEqual 14
        LevelOfDetail.LOD15.bingLOD shouldEqual 15
        LevelOfDetail.LOD16.bingLOD shouldEqual 16
        LevelOfDetail.LOD17.bingLOD shouldEqual 17
        LevelOfDetail.LOD18.bingLOD shouldEqual 18
    }

    "prepar3dLOD returns Prepar3D level of detail as number" {
        LevelOfDetail.LOD14.prepar3dLOD shouldEqual 12
        LevelOfDetail.LOD15.prepar3dLOD shouldEqual 13
        LevelOfDetail.LOD16.prepar3dLOD shouldEqual 14
        LevelOfDetail.LOD17.prepar3dLOD shouldEqual 15
        LevelOfDetail.LOD18.prepar3dLOD shouldEqual 16
    }

})