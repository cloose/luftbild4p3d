package org.luftbild4p3d.bing.types

enum class LevelOfDetail(val bingLOD: Int, val prepar3dLOD: Int, val mapSize: Long) {

    LOD13(13, 11, 256L shl 13),
    LOD14(14, 12, 256L shl 14),
    LOD15(15, 13, 256L shl 15),
    LOD16(16, 14, 256L shl 16),
    LOD17(17, 15, 256L shl 17),
    LOD18(18, 16, 256L shl 18),
    LOD19(19, 17, 256L shl 19)

}