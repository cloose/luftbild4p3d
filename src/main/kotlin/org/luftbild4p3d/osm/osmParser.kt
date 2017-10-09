package org.luftbild4p3d.osm

import java.io.File
import java.io.StringReader
import javax.xml.bind.JAXBContext

fun parseOsmData(osmData: String): Osm {
    val jaxbContext = JAXBContext.newInstance(Osm::class.java)
    val unmarshaller = jaxbContext.createUnmarshaller()

    return unmarshaller.unmarshal(StringReader(osmData)) as Osm
}

fun parseOsmDataFile(osmDataFileName: String): Osm {
    println("Parse OSM data from file: $osmDataFileName")

    val jaxbContext = JAXBContext.newInstance(Osm::class.java)
    val unmarshaller = jaxbContext.createUnmarshaller()

    val osmDataFile = File("$osmDataFileName")
    osmDataFile.reader().use {
        return unmarshaller.unmarshal(it) as Osm
    }
}
