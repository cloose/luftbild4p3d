package org.luftbild4p3d.app

import org.junit.Test
import org.luftbild4p3d.bing.LevelOfDetail
import kotlin.test.assertEquals

class WorkFolderTest {

    @Test
    fun assemblesWorkFolderNameFromPositiveLatitudeAndLongitude() {
        val workFolder = WorkFolder("", 54, 9, LevelOfDetail.LOD16)

        assertEquals("+54+009", workFolder.name)
    }

    @Test
    fun assemblesWorkFolderNameFromNegativeLatitudeAndLongitude() {
        val workFolder = WorkFolder("", -8, -21, LevelOfDetail.LOD16)

        assertEquals("-08-021", workFolder.name)
    }

    @Test
    fun assemblesImageFolderNameFromLevelOfDetail() {
        val workFolder = WorkFolder("", 54, 9, LevelOfDetail.LOD16)

        assertEquals("BI16", workFolder.imageFolderName)
    }

    @Test
    fun assemblesImageFolderPathFromWorkFolderNameAndImageFolderName() {
        val workFolder = WorkFolder("", 54, 9, LevelOfDetail.LOD16)

        assertEquals("+54+009/BI16", workFolder.imageFolderPath)
    }

    @Test
    fun assemblesSceneryFolderName() {
        val workFolder = WorkFolder("", 54, 9, LevelOfDetail.LOD16)

        assertEquals("scenery", workFolder.sceneryFolderName)
    }

    @Test
    fun assemblesSceneryFolderPathFromWorkFolderNameAndSceneryFolderName() {
        val workFolder = WorkFolder("", 54, 9, LevelOfDetail.LOD16)

        assertEquals("+54+009/scenery", workFolder.sceneryFolderPath)
    }

    @Test
    fun assemblesTextureFolderName() {
        val workFolder = WorkFolder("", 54, 9, LevelOfDetail.LOD16)

        assertEquals("texture", workFolder.textureFolderName)
    }

    @Test
    fun assemblesTextureFolderPathFromWorkFolderNameAndTextureFolderName() {
        val workFolder = WorkFolder("", 54, 9, LevelOfDetail.LOD16)

        assertEquals("+54+009/texture", workFolder.textureFolderPath)
    }

}