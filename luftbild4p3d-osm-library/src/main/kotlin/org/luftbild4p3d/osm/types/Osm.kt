package org.luftbild4p3d.osm.types

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
data class Osm(@field:XmlElement val node: MutableList<Node> = mutableListOf(),
               @field:XmlElement val way: MutableList<Way> = mutableListOf(),
               @field:XmlElement val relation: MutableList<Relation> = mutableListOf())
