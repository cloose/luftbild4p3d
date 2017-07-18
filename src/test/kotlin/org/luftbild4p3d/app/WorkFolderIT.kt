package org.luftbild4p3d.app

import org.junit.Before
import org.junit.Test
import org.luftbild4p3d.bing.LevelOfDetail
import java.io.File
import kotlin.test.assertTrue

class WorkFolderIT {

    val prefix = "test"
    val workFolderName = "$prefix/+54+009"

    @Before
    fun setup() {
        File("$prefix/$workFolderName").deleteRecursively()
    }

    @Test
    fun createsWorkFolder() {
        val workFolder = WorkFolder(prefix, 54, 9, LevelOfDetail.LOD16)

        workFolder.setup()

        assertTrue { File(workFolderName).exists() }
    }

    @Test
    fun createsImageFolderInsideWorkFolder() {
        val workFolder = WorkFolder(prefix, 54, 9, LevelOfDetail.LOD16)

        workFolder.setup()

        assertTrue { File("$workFolderName/BI16").exists() }
    }

    @Test
    fun createsSceneryFolderInsideWorkFolder() {
        val workFolder = WorkFolder(prefix, 54, 9, LevelOfDetail.LOD16)

        workFolder.setup()

        assertTrue { File("$workFolderName/scenery").exists() }
    }

    @Test
    fun createsTextureFolderInsideWorkFolder() {
        val workFolder = WorkFolder(prefix, 54, 9, LevelOfDetail.LOD16)

        workFolder.setup()

        assertTrue { File("$workFolderName/texture").exists() }
    }
}