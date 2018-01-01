package org.luftbild4p3d.osm.types

import io.kotlintest.matchers.haveSize
import io.kotlintest.matchers.should
import io.kotlintest.matchers.shouldEqual
import io.kotlintest.specs.StringSpec
import java.io.StringReader
import java.math.BigDecimal
import javax.xml.bind.JAXBContext

class OsmTest : StringSpec ({

    "Osm can be unmarshalled from XML data" {
        val osmData = """<?xml version="1.0" encoding="UTF-8"?>
            <osm version="0.6" generator="Overpass API">
                <meta osm_base="2017-07-23T07:12:02Z"/>
                <node id="1" lat="52.9544335" lon="9.0289179"/>
                <node id="2" lat="52.9272110" lon="8.9999792"/>
                <way id="3">
                    <nd ref="1" lat="52.9544335" lon="9.0289179"/>
                    <nd ref="2" lat="52.9272110" lon="8.9999792"/>
                    <tag k="natural" v="water"/>
                </way>
                <relation id="4">
                    <member type="way" ref="3" role="inner">
                        <nd ref="1" lat="52.9544335" lon="9.0289179"/>
                        <nd ref="2" lat="52.9272110" lon="8.9999792"/>
                    </member>
                    <tag k="natural" v="water"/>
                </relation>
            </osm>"""
        val jaxbContext = JAXBContext.newInstance(Osm::class.java)
        val unmarshaller = jaxbContext.createUnmarshaller()

        val osm = unmarshaller.unmarshal(StringReader(osmData)) as Osm

        osm.node should haveSize(2)
        osm.node[0].id shouldEqual 1L
        osm.node[0].latitude shouldEqual BigDecimal.valueOf(52.9544335)
        osm.node[0].longitude shouldEqual BigDecimal.valueOf(9.0289179)
        osm.node[1].id shouldEqual 2L
        osm.node[1].latitude shouldEqual BigDecimal.valueOf(52.927211).setScale(7)
        osm.node[1].longitude shouldEqual BigDecimal.valueOf(8.9999792)

        osm.way should haveSize(1)
        osm.way[0].id shouldEqual 3L
        osm.way[0].nodeReference should haveSize(2)
        osm.way[0].nodeReference[0].ref shouldEqual 1L
        osm.way[0].nodeReference[0].latitude shouldEqual BigDecimal.valueOf(52.9544335)
        osm.way[0].nodeReference[0].longitude shouldEqual BigDecimal.valueOf(9.0289179)
        osm.way[0].nodeReference[1].ref shouldEqual 2L
        osm.way[0].nodeReference[1].latitude shouldEqual BigDecimal.valueOf(52.927211).setScale(7)
        osm.way[0].nodeReference[1].longitude shouldEqual BigDecimal.valueOf(8.9999792)
        osm.way[0].tag should haveSize(1)
        osm.way[0].tag[0].key shouldEqual "natural"
        osm.way[0].tag[0].value shouldEqual "water"

        osm.relation should haveSize(1)
        osm.relation[0].id shouldEqual 4L
        osm.relation[0].member should haveSize(1)
        osm.relation[0].member[0].type shouldEqual "way"
        osm.relation[0].member[0].ref shouldEqual 3L
        osm.relation[0].member[0].role shouldEqual "inner"
        osm.relation[0].member[0].nodeReference should haveSize(2)
        osm.relation[0].member[0].nodeReference[0].ref shouldEqual 1L
        osm.relation[0].member[0].nodeReference[0].latitude shouldEqual BigDecimal.valueOf(52.9544335)
        osm.relation[0].member[0].nodeReference[0].longitude shouldEqual BigDecimal.valueOf(9.0289179)
        osm.relation[0].member[0].nodeReference[1].ref shouldEqual 2L
        osm.relation[0].member[0].nodeReference[1].latitude shouldEqual BigDecimal.valueOf(52.927211).setScale(7)
        osm.relation[0].member[0].nodeReference[1].longitude shouldEqual BigDecimal.valueOf(8.9999792)
        osm.relation[0].tag should haveSize(1)
        osm.relation[0].tag[0].key shouldEqual "natural"
        osm.relation[0].tag[0].value shouldEqual "water"
    }

})