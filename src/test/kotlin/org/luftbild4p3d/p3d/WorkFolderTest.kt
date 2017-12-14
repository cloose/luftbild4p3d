package org.luftbild4p3d.p3d

import io.kotlintest.TestCaseContext
import io.kotlintest.matchers.*
import io.kotlintest.specs.StringSpec
import org.luftbild4p3d.bing.LevelOfDetail
import org.luftbild4p3d.bing.Wgs84Coordinates
import java.io.File

class WorkFolderTest : StringSpec({

    "path is assembled from prefix and positive latitude and longitude" {
        val workFolder = WorkFolder("test", Area(Wgs84Coordinates(54.0, 9.0), LevelOfDetail.LOD16))

        workFolder.path shouldEqual "test/+54+009"
    }

    "path is assembled from prefix and negative latitude and longitude" {
        val workFolder = WorkFolder("test", Area(Wgs84Coordinates(-8.0, -21.0), LevelOfDetail.LOD16))

        workFolder.path shouldEqual "test/-08-021"
    }

    "imageFolderName is assembled from level of detail" {
        val workFolder = WorkFolder("test", Area(Wgs84Coordinates(54.0, 9.0), LevelOfDetail.LOD16))

        workFolder.imageFolderName shouldEqual "BI16"
    }

    "imageFolderPath is assembled from path and imageFolderName" {
        val workFolder = WorkFolder("test", Area(Wgs84Coordinates(54.0, 9.0), LevelOfDetail.LOD16))

        workFolder.imageFolderPath shouldEqual "test/+54+009/BI16"
    }

    "sceneryFolderName is constant string 'scenery'" {
        val workFolder = WorkFolder("test", Area(Wgs84Coordinates(54.0, 9.0), LevelOfDetail.LOD16))

        workFolder.sceneryFolderName shouldEqual "scenery"
    }

    "sceneryFolderPath is assembled from path and sceneryFolderName" {
        val workFolder = WorkFolder("test", Area(Wgs84Coordinates(54.0, 9.0), LevelOfDetail.LOD16))

        workFolder.sceneryFolderPath shouldEqual "test/+54+009/scenery"
    }

    "textureFolderName is constant string 'texture'" {
        val workFolder = WorkFolder("test", Area(Wgs84Coordinates(54.0, 9.0), LevelOfDetail.LOD16))

        workFolder.textureFolderName shouldEqual "texture"
    }

    "textureFolderPath is assembled from path and textureFolderName" {
        val workFolder = WorkFolder("test", Area(Wgs84Coordinates(54.0, 9.0), LevelOfDetail.LOD16))

        workFolder.textureFolderPath shouldEqual "test/+54+009/texture"
    }

    "setupWorkFolders creates work folder" {
        val workFolder = WorkFolder("test", Area(Wgs84Coordinates(54.0, 9.0), LevelOfDetail.LOD16))

        setupWorkFolders(workFolder)

        File("test/+54+009") should exist()
    }

    "setupWorkFolders creates image folder inside work folder" {
        val workFolder = WorkFolder("test", Area(Wgs84Coordinates(54.0, 9.0), LevelOfDetail.LOD16))

        setupWorkFolders(workFolder)

        File("test/+54+009/BI16") should exist()
    }

    "setupWorkFolders creates scenery folder inside work folder" {
        val workFolder = WorkFolder("test", Area(Wgs84Coordinates(54.0, 9.0), LevelOfDetail.LOD16))

        setupWorkFolders(workFolder)

        File("test/+54+009/scenery") should exist()
    }

    "setupWorkFolders creates texture folder inside work folder" {
        val workFolder = WorkFolder("test", Area(Wgs84Coordinates(54.0, 9.0), LevelOfDetail.LOD16))

        setupWorkFolders(workFolder)

        File("test/+54+009/texture") should exist()
    }
}) {
    override fun interceptTestCase(context: TestCaseContext, test: () -> Unit) {
        File("test").deleteRecursively()

        test()
    }
}

fun exist() = object : Matcher<File> {
    override fun test(value: File) = Result(value.exists(), "$value should exist")
}