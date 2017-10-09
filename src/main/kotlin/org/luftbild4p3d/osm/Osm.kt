package org.luftbild4p3d.osm

import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
class Osm() {

    @XmlElement(name = "node")
    val node: MutableCollection<Node> = mutableListOf()

    @XmlElement(name = "way")
    val way: MutableCollection<Way> = mutableListOf()

    @XmlElement(name = "relation")
    val relation: MutableCollection<Relation> = mutableListOf()

}